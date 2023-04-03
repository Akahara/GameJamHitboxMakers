package fr.ttl.game.audio;

import static org.lwjgl.openal.AL10.AL_FORMAT_MONO16;
import static org.lwjgl.openal.AL10.AL_FORMAT_STEREO16;
import static org.lwjgl.openal.AL10.alBufferData;
import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.AL10.alGenBuffers;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_close;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_get_info;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_get_samples_short_interleaved;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_open_memory;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_stream_length_in_samples;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBVorbisInfo;

import fr.ttl.game.Logger;

public class Sound {
	
	private int bufferId;
	private float duration;
	
	private Sound(int bufferId, float duration) {
		this.bufferId = bufferId;
		this.duration = duration;
		AudioManager.pollALErrors();
	}
	
	int getBufferId() {
		return bufferId;
	}
	
	public float getDuration() {
		return duration;
	}
	
	public void dispose() {
		AudioManager.pollALErrors();
		alDeleteBuffers(bufferId);
		bufferId = 0;
		AudioManager.pollALErrors();
	}
	
	public static Sound loadSound(String resourcePath) {
		try {
			if(resourcePath.endsWith(".ogg"))
				return loadVorbisSound(resourcePath);
			throw new IOException("No parser for extension of file " + resourcePath);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public static Sound[] loadSounds(String path) {
		List<Sound> sounds = new ArrayList<>();
		int i = 1;
		try {
			for(;; i++)
				sounds.add(loadSound(String.format(path, i)));
		} catch (RuntimeException e) {
//			Logger.err("Could not load '" + String.format(path, i) + "': " + e.getMessage());
		}
		if(sounds.isEmpty())
			throw new IllegalArgumentException("Could not load " + path);
		Logger.log("Loaded " + sounds.size() + " sounds for '" + path + "'");
		return sounds.toArray(new Sound[sounds.size()]);
	}
	
	private static Sound loadVorbisSound(String resourcePath) throws IOException {
		int alBuffer = alGenBuffers();
		
		try(STBVorbisInfo info = STBVorbisInfo.malloc()) {
			ByteBuffer vorbis = loadResourceToBuffer(resourcePath);
			IntBuffer error = BufferUtils.createIntBuffer(1);
			long decoder = stb_vorbis_open_memory(vorbis, error, null);
			
			if (decoder == NULL)
				throw new IOException("Failed to open Ogg Vorbis file. Error: " + error.get(0));
			stb_vorbis_get_info(decoder, info);
			int channels = info.channels();
			ShortBuffer pcm = BufferUtils.createShortBuffer(stb_vorbis_stream_length_in_samples(decoder) * channels);
//			int limit = info.sample_rate()*5;
//			if(limit < pcm.capacity())
//				pcm.limit(limit); // FIX remove
			stb_vorbis_get_samples_short_interleaved(decoder, channels, pcm);
			stb_vorbis_close(decoder);
			alBufferData(alBuffer, info.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, pcm, info.sample_rate());
			AudioManager.pollALErrors();
			float duration = (float)stb_vorbis_stream_length_in_samples(decoder) / info.sample_rate();
			
			return new Sound(alBuffer, duration);
		}
	}
	
	private static ByteBuffer loadResourceToBuffer(String path) throws IOException {
		try (	InputStream source = Sound.class.getResourceAsStream(path);
				ReadableByteChannel rbc = Channels.newChannel(source)) {
			
			ByteBuffer buffer = BufferUtils.createByteBuffer(32 * 1024);
			
			while (true) {
				int bytes = rbc.read(buffer);
				if (bytes == -1) {
					break;
				}
				if (buffer.remaining() == 0) {
					ByteBuffer newBuffer = BufferUtils.createByteBuffer(buffer.capacity() * 3 / 2);
					buffer.flip();
					newBuffer.put(buffer);
					buffer = newBuffer;
				}
			}
			
			buffer.flip();
			return buffer;
		} catch (NullPointerException e) {
			throw new IOException("Resource '" + path + "' does not exist");
		}
	}
	
}

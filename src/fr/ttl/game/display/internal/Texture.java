package fr.ttl.game.display.internal;

import fr.ttl.game.display.WindowManager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL30.*;

public class Texture {

	public static Texture DUMMY_TEXTURE;

	public final String path;
	public final int width, height;
	public final int id;
	
	private Texture(String path, int id, int width, int height) {
		this.path = path;
		this.id = id;
		this.width = width;
		this.height = height;
	}
	
	public static Texture loadTexture(String path) throws IOException {
		int id, width, height;

		try (InputStream is = Texture.class.getResourceAsStream(path)) {
			if (is == null)
				throw new IOException("Resource " + path + " does not exist");
			BufferedImage image = ImageIO.read(is);

			width = image.getWidth();
			height = image.getHeight();

			int size = width * height;
			int[] data = new int[size];

			image.getRGB(0, 0, width, height, data, 0, width);

			int[] px = new int[size];
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					int pos = i * width + j;
					int a = (data[pos] & 0xff000000) >> 24;
					int r = (data[pos] & 0x00ff0000) >> 16;
					int g = (data[pos] & 0x0000ff00) >> 8;
					int b = (data[pos] & 0x000000ff);
					px[(height - 1 - i) * width + j] =
							a << 24 |
									b << 16 |
									g << 8 |
									r;
				}
			}

			id = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, id);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, px);
		}
			
		return new Texture(path, id, width, height);
	}
	
	public static Texture fromBuffer(int width, int height, ByteBuffer buffer) {
		int id = glGenTextures();
		WindowManager.pollGLError();
		glBindTexture(GL_TEXTURE_2D, id);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		WindowManager.pollGLError();
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		WindowManager.pollGLError();
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		WindowManager.pollGLError();
		return new Texture(null, id, width, height);
	}
	
	public void enableAntialiasing() {
		bind(0);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	}
	
	public void enableWrapping() {
		bind(0);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
	}

	public void bind(int slot) {
		glActiveTexture(GL_TEXTURE0 + slot);
		glBindTexture(GL_TEXTURE_2D, id);
	}
	
	public void dispose() {
		glDeleteTextures(id);
	}
}

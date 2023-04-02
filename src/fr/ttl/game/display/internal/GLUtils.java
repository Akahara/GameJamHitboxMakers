package fr.ttl.game.display.internal;


import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.stream.IntStream;

import fr.ttl.game.display.Color;

public class GLUtils {
	
	/**
	 * @param size the size in bytes of the buffer
	 */
	public static ByteBuffer createBuffer(int size) {
		return ByteBuffer.allocateDirect(size).order(ByteOrder.LITTLE_ENDIAN);
	}
	
	/**
	 * Creates an index buffer containing the indices for n quads.
	 */
	public static IndexBuffer createQuadIndexBuffer(int quadCount) {
		return new IndexBuffer(createQuadIndices(quadCount));
	}

	/**
	 * Creates an the indices needed to draw n quads.
	 * The returned array contains 6 times the number of quads as indices.
	 */
	public static int[] createQuadIndices(int quadCount) {
		int[] indices = new int[quadCount*6];
		for(int i = 0; i < quadCount; i++) {
			// 3-2
			// |/|
			// 0-1
			indices[6*i+0] = 4*i+0; // 0
			indices[6*i+1] = 4*i+1; // 1
			indices[6*i+2] = 4*i+2; // 2
			indices[6*i+3] = 4*i+2; // 2
			indices[6*i+4] = 4*i+3; // 3
			indices[6*i+5] = 4*i+0; // 0
		}
		return indices;
	}
	
	public static int[] createLineIndices(int lineCount) {
		int[] indices = new int[lineCount*2];
		for(int i = 0; i < indices.length; i++)
			indices[i] = i;
		return indices;
	}
	
	public static float[] createQuadVertices(float minX, float maxX) {
		return new float[] {
				minX, minX,
				maxX, minX,
				maxX, maxX,
				minX, maxX,
		};
	}
	
	public static int[] createTrianglesIndices(int triangleCount) {
//		int[] indices = new int[triangleCount*3];
//		for(int i = 0; i < indices.length; i++)
//			indices[i] = i;
//		return indices;
		return IntStream.range(0, triangleCount*3).toArray();
	}
	
	public static void enableBlend(boolean enable) {
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		if(enable)
			glEnable(GL_BLEND);
		else
			glDisable(GL_BLEND);
	}

	public static void clearColor(Color c) {
		glClearColor(c.r, c.g, c.b, c.a);
	}
	
	public static void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	public static void clearDepth() {
		glClear(GL_DEPTH_BUFFER_BIT);
	}
	
	public static void dcTriangles(int triangleCount) {
		glDrawElements(GL_TRIANGLES, triangleCount*3, GL_UNSIGNED_INT, NULL);
	}

	public static void dcQuads(int quadCount) {
		dcTriangles(quadCount*2);
	}
	
	public static void dcLines(int lineCount) {
		glDrawElements(GL_LINES, lineCount*2, GL_UNSIGNED_INT, NULL);
	}
	
}

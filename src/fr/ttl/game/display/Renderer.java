package fr.ttl.game.display;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.system.MemoryUtil.NULL;

import fr.ttl.game.Main;
import fr.ttl.game.display.internal.Shader;
import fr.ttl.game.display.internal.Texture;
import fr.ttl.game.display.internal.VertexArray;
import fr.ttl.game.math.Mathf;
import fr.ttl.game.math.Mathr;

public class Renderer {

    static Shader texturedShader;
    static Shader simpleQuadShader;
    static Shader complexQuadShader;
    static Shader transitionShader;
    public static Shader TRADER_GLOW_SHADER, BG_SHADER, BLIT_SHADER;

    private static float camX, camY, camW, camH;

    private static void setCameraUniform(Shader shader) {
        shader.setUniform4f("u_camera", camX, camY, camW, camH);
    }

    static void setTextCameraUniform(Shader fontShader) {
        fontShader.setUniform4f("u_camera", camX, camY, camW, camH);
    }

    static void setColorUniform(Shader shader, String uniformName, Color color) {
        shader.setUniform4f(uniformName, color.r, color.g, color.b, color.a);
    }

    public static void setCamera(float x, float y, float width, float height,
                                 float screenX, float screenY, float screenWidth, float screenHeight) {
        camX = screenX - x*screenWidth/width;
        camY = screenY - y*screenHeight/height;
        camW = screenWidth/width;
        camH = screenHeight/height;
    }

    public static void drawQuad(float x, float y, float width, float height, Color color) {
        VertexArray.simpleQuad.bind();
        simpleQuadShader.bind();
        simpleQuadShader.setUniform4f("u_color", color.r, color.g, color.b, color.a);
        simpleQuadShader.setUniform4f("u_position", x, y, width, height);
        setCameraUniform(simpleQuadShader);
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, NULL);
    }
    
    public static void drawTransition(float sceneTime, Texture current, Texture next) {
        VertexArray.simpleQuad.bind();
        setCamera(0, 0, 1, 1, 0, 0, 1, 1);
        transitionShader.bind();
        transitionShader.setUniform1f("u_time", sceneTime);
        transitionShader.setUniform4f("u_position", 0, 0, 1, 1);
        setCameraUniform(transitionShader);
        current.bind(0);
        next.bind(1);
        Textures.NOISE.bind(2);
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, NULL);
    }
    
    public static void drawCursor(float x, float y) {
    	float size = 2f;
    	float t = System.currentTimeMillis()*.0001f;
    	t *= 4;
    	float it = (int)t;
    	t = Mathf.lerp(t-it, Mathr.rand1(it), Mathr.rand1(it+1));
    	t = Mathf.lerp(t, .25f, 1.f);
    	drawQuad(x-size*.5f, y-size*.5f, size, size, Textures.CURSOR, new Color(1,1,1, t));
    }
    
    public static void drawQuad(float x, float y, float width, float height, Texture texture) {
        drawQuad(x, y, width, height, 0, 0, 1, 1, texture);
    }

    public static void drawQuad(float x, float y, float width, float height, float texX, float texY, float texW, float texH, Texture texture) {
        drawQuad(x, y, width, height, texX, texY, texW, texH, texture, Color.white);
    }

    public static void drawQuad(float x, float y, float width, float height, Texture texture, Color color) {
        drawQuad(x, y, width, height, 0, 0, 1, 1, texture, color);
    }

    public static void drawQuad(float x, float y, float width, float height, float texX, float texY, float texW, float texH, Texture texture, Color color) {
        VertexArray.simpleQuad.bind();
        texture.bind(0);
        texturedShader.bind();
        texturedShader.setUniform4f("u_position", x, y, width, height);
        texturedShader.setUniform4f("u_tex", texX, texY, texW, texH);
        setColorUniform(texturedShader, "u_color", color);
        setCameraUniform(texturedShader);
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, NULL);
    }

    public static void drawQuad(float x, float y, float width, float height, Texture texture, Color color, Shader shader) {
        VertexArray.simpleQuad.bind();
        if(texture != null)
        	texture.bind(0);
        shader.bind();
        shader.setUniform4f("u_position", x, y, width, height);
        shader.setUniform4f("u_tex", 0,0,1,1);
        if(color != null)
        	setColorUniform(shader, "u_color", color);
        setCameraUniform(shader);
        shader.setUniform1f("u_time", Main.appTime());
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, NULL);
    }

    public static void drawComplexQuad(float x, float y, float width, float height, float rotation, Texture texture, Color color) {
        VertexArray.simpleQuad.bind();
        texture.bind(0);
        complexQuadShader.bind();
        complexQuadShader.setUniform4f("u_position", x, y, width, height);
        complexQuadShader.setUniform1f("u_rotation", rotation);
        setColorUniform(complexQuadShader, "u_color", color);
        setCameraUniform(complexQuadShader);
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, NULL);
    }

}

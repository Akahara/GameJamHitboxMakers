package fr.ttl.game.display;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.IOException;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import fr.ttl.game.Logger;
import fr.ttl.game.display.internal.Shader;
import fr.ttl.game.display.internal.Texture;
import fr.ttl.game.display.internal.VertexArray;
import fr.ttl.game.io.PlayerInputs;

public class WindowManager {

    private static final int DISPLAY_RATIO_W = 16, DISPLAY_RATIO_H = 9;
    public static final float DISPLAY_RATIO = (float) DISPLAY_RATIO_W/DISPLAY_RATIO_H;

    public static long window;
    private static int winWidth, winHeight;

    private static Shader stencilShader;

    public static void createWindow(String title) {
        winWidth = 1600;
        winHeight = 900;

        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW !");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_SAMPLES, 4);
        glfwWindowHint(GLFW_FOCUS_ON_SHOW, GLFW_TRUE);

        window = glfwCreateWindow(winWidth, winHeight, title, NULL, NULL);

        if (window == NULL)
            throw new IllegalStateException("Unable to create a window !");

        glfwMakeContextCurrent(window);
        glfwSetWindowAspectRatio(window, DISPLAY_RATIO_W, DISPLAY_RATIO_H);
        showCursor(false);
        
        GL.createCapabilities();
//        glClearColor(0, 0, 1, 1);
        glClearStencil(0);
        glStencilFunc(GL_EQUAL, 0xff, 0xff);
        glEnable(GL_BLEND);
        glEnable(GL_MULTISAMPLE);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glfwSwapInterval(1); // enable vsync, drastically slows down the game if
                            // the refresh rate of the monitor is low. Would not be
                            // an issue if the game was multithreaded with glfwSwapBuffers
                            // on one and everything else on the other

        pollGLError();

        VertexArray.simpleQuad.bind();
        pollGLError();
        try {
            loadGLResources();
        } catch (IOException e) {
            throw new IllegalStateException("Could not read all GL resources", e);
        }
        VertexArray.unbind();

        pollGLError();

        glfwSetWindowSizeCallback(window, (win, w, h) -> {
            glViewport(0, 0, w, h);
            winWidth = w;
            winHeight = h;
        });

        glfwSetKeyCallback(window, (win, key, scanCode, action, mods) -> {
            if(action == GLFW_PRESS && key == GLFW_KEY_ESCAPE)
                glfwSetWindowShouldClose(window, true);
        });

        PlayerInputs.initInputs(window);
    }

    private static void loadGLResources() throws IOException {
        Renderer.texturedShader = Shader.compileAndCreateShader("/shaders/default_textured.vert", "/shaders/default_textured.frag");
        Renderer.simpleQuadShader = Shader.compileAndCreateShader("/shaders/simple_quad.vert", "/shaders/simple_quad.frag");
        Renderer.complexQuadShader = Shader.compileAndCreateShader("/shaders/complex_quad.vert", "/shaders/complex_quad.frag");
        Renderer.transitionShader = Shader.compileAndCreateShader("/shaders/transition.vert", "/shaders/transition.frag");
        Renderer.TRADER_GLOW_SHADER = Shader.compileAndCreateShader("/shaders/glow.vert", "/shaders/glow.frag");
        Renderer.BG_SHADER = Shader.compileAndCreateShader("/shaders/background.vert", "/shaders/background.frag");
        Renderer.BLIT_SHADER = Shader.compileAndCreateShader("/shaders/blit.vs", "/shaders/blit.fs");
        
        Renderer.transitionShader.bind();
        Renderer.transitionShader.setUniform1i("u_next", 1);
        Renderer.transitionShader.setUniform1i("u_noise", 2);

        pollGLError();

        Texture.DUMMY_TEXTURE = Texture.loadTexture("/textures/dummy.png");

        pollGLError();

        Logger.log("Loaded gl resources");
    }
	
	public static void show(boolean fullScreen) {
		glfwShowWindow(window);
		glfwFocusWindow(window);
		if(fullScreen) {
			long monitor = glfwGetPrimaryMonitor();
			int[] width = new int[1], height = new int[1];
			int[] xpos = new int[1], ypos = new int[1];
			glfwGetMonitorWorkarea(monitor, xpos, ypos, width, height);
			glfwSetWindowMonitor(window, glfwGetPrimaryMonitor(), 0, 0, width[0], height[0], GLFW_DONT_CARE);
		}
	}
	
	public static void showCursor(boolean visible) {
        glfwSetInputMode(window, GLFW_CURSOR, visible ? GLFW_CURSOR_NORMAL : GLFW_CURSOR_HIDDEN);
	}

    public static boolean shouldDispose() {
        return glfwWindowShouldClose(window);
    }

    public static void setWindowShouldDispose(boolean shouldClose) {
        glfwSetWindowShouldClose(window, shouldClose);
    }

    public static void dispose() {
        glfwTerminate();
        window = 0;
    }

    public static void setWindowTitle(String title) {
        glfwSetWindowTitle(window, title);
    }

    public static void beginFrame() {
        pollGLError();
        glClear(GL_COLOR_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
        clearStencil();
    }

    public static void endFrame() {
        pollGLError();
        clearStencil();
        glfwSwapBuffers(window);
        pollGLError();
    }

    public static void setStencil(float x, float y, float width, float height) {
        VertexArray.simpleQuad.bind();
        glClear(GL_STENCIL_BUFFER_BIT);
        glEnable(GL_STENCIL_TEST);
        stencilShader.bind();
        stencilShader.setUniform4f("u_position", x, y, width, height);
        glStencilOp(GL_REPLACE, GL_REPLACE, GL_REPLACE);
        glColorMask(true, false, false, false);
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, NULL);
        glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);
        glColorMask(true, true, true, true);
    }

    public static void clearStencil() {
        glDisable(GL_STENCIL_TEST);
    }

    public static int getWinWidth() {
        return winWidth;
    }

    public static int getWinHeight() {
        return winHeight;
    }

    public static void pollGLError() {
        int err = glGetError();
        if (err != 0)
            Logger.err("GL error : " + err + " " + new Exception().getStackTrace()[1]);
    }
}

#version 330 core

in vec2 i_position;

out vec2 v_texCoord;
out float v_pointy;

uniform vec4 u_camera;
uniform vec4 u_position;
uniform vec4 u_tex;

void main() {
	vec2 world = i_position*u_position.zw+u_position.xy;
	vec2 view = world*u_camera.zw+u_camera.xy;
	vec2 screen = view*2-1;
	gl_Position = vec4(screen, 0, 1);
	v_texCoord = i_position*u_tex.zw+u_tex.xy;
	v_pointy = i_position.y;
}

#version 330 core

out vec4 color;

uniform sampler2D u_current, u_next, u_noise;
uniform float u_time;

in vec2 uv;

void main() {
	vec4 current = texture(u_current, uv);
	vec4 old = texture(u_next, uv);
	color = vec4(0);
	float t = u_time*2;
	float n = texture(u_noise, uv).r;
	color += smoothstep(.5, .4, t-n) * old;
	color += smoothstep(.5, .6, t-n) * current;
}
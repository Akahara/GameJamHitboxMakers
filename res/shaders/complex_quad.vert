#version 330 core

in vec2 i_position;

out vec2 v_texCoord;

uniform vec4 u_camera;
uniform vec4 u_position;
uniform float u_rotation;

mat2 rot(float theta) {
    return mat2(
        cos(theta), sin(theta),
        -sin(theta), cos(theta));
}

void main() {
    vec2 world = rot(u_rotation) * (i_position - .5) + .5;
    world = world*u_position.zw+u_position.xy;
    vec2 view = world*u_camera.zw+u_camera.xy;
    vec2 screen = view*2-1;
    gl_Position = vec4(screen, 0, 1);
    v_texCoord = i_position;
}
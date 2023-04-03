#version 330 core

#define PI 3.14159265

uniform sampler2D u_originTexture;

in vec2 v_uv;

uniform float u_minResource = 10;
uniform float u_time;

layout(location=0) out vec4 color;

void main(void) {
  vec2 uv = v_uv;
  uv *=  1.0 - uv.yx;
  float v = uv.x*uv.y * 15.0;
  v = pow(v, 0.25);
  float r = 1./(1.+exp(-2.*(2.5-u_minResource)));
  r *= max(cos(PI*2.*u_time*.4), 0) * .6 + .2;
  v = (1.-v)*(1.-r)+v;
  color = texture(u_originTexture, v_uv);
  color = vec4(1.-v, .1, .1, 1)*(1.-v) + color*v;
}

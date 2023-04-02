#version 330 core

layout(location=0) out vec4 color;

in vec2 v_texCoord;

uniform float u_time;
uniform vec2 u_dir;

float noise(int x,int y)
{   
    float fx = float(x);
    float fy = float(y);
    
    return 2.0 * fract(sin(dot(vec2(fx, fy) ,vec2(12.9898,78.233))) * 43758.5453) - 1.0;
}

float smoothNoise(int x,int y)
{
    return noise(x,y)/4.0+(noise(x+1,y)+noise(x-1,y)+noise(x,y+1)+noise(x,y-1))/8.0+(noise(x+1,y+1)+noise(x+1,y-1)+noise(x-1,y+1)+noise(x-1,y-1))/16.0;
}

float COSInterpolation(float x,float y,float n)
{
    float r = n*3.1415926;
    float f = (1.0-cos(r))*0.5;
    return x*(1.0-f)+y*f;
    
}

float InterpolationNoise(float x, float y)
{
    int ix = int(x);
    int iy = int(y);
    float fracx = x-float(int(x));
    float fracy = y-float(int(y));
    
    float v1 = smoothNoise(ix,iy);
    float v2 = smoothNoise(ix+1,iy);
    float v3 = smoothNoise(ix,iy+1);
    float v4 = smoothNoise(ix+1,iy+1);
    
   	float i1 = COSInterpolation(v1,v2,fracx);
    float i2 = COSInterpolation(v3,v4,fracx);
    
    return COSInterpolation(i1,i2,fracy);
}

void main() {
	vec2 uv = v_texCoord;
	uv *= u_dir;
	uv += 1;
	uv *= 10.;
	uv += u_time * .3;
	float a = InterpolationNoise(uv.x, uv.y);
	a *= 10;
	a += u_time * .2;
	float b = InterpolationNoise(uv.x*1.4+a, uv.y*.6+a*2);
	b = mix(.1, .4, b);
	color = vec4(b,b,b,1);
	
}
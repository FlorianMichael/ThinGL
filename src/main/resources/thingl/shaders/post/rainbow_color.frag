#version 330 core

uniform sampler2D u_Mask;
uniform float u_Time;
uniform float u_SpeedDivider;
uniform float u_RainbowDivider;
uniform float u_Offset;
uniform vec2 u_Direction;

in vec2 v_RelTexCoords;
in vec2 v_VpTexCoords;
out vec4 o_Color;

// https://gist.github.com/983/e170a24ae8eba2cd174f
vec3 hsv2rgb(vec3 c);
vec3 rgb2hsv(vec3 c);

void main() {
    vec4 maskPixel = texture(u_Mask, v_VpTexCoords);
    if (maskPixel.a != 0.0) {
        vec2 position = v_RelTexCoords / u_RainbowDivider * u_Direction;
        vec3 hsv = rgb2hsv(maskPixel.rgb);
        o_Color = vec4(hsv2rgb(vec3(position.x + position.y + u_Offset - u_Time / u_SpeedDivider, hsv.y, hsv.z)), maskPixel.a);
        return;
    }

    discard;
}

vec3 rgb2hsv(vec3 c) {
    vec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);
    vec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));
    vec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));

    float d = q.x - min(q.w, q.y);
    float e = 1.0e-10;
    return vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);
}

vec3 hsv2rgb(vec3 c) {
    vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}

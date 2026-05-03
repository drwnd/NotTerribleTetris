#version 460 core

in vec2 fragTextureCoordinate;

out vec4 fragColor;

uniform vec4 color;
uniform sampler2D blockTexture;

void main() {
    vec4 blockColor = texture(blockTexture, fragTextureCoordinate);
    if (blockColor.a == 0) discard;
    fragColor = vec4(blockColor.rgb, 1) * color;
}
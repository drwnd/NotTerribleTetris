#version 460 core

uniform vec2 position;
uniform vec2 size;
uniform float aliveSeconds;
uniform float gravity;
uniform float rotationSpeed;
uniform vec2 velocity;

layout (location = 0) in vec2 positionOffset;
layout (location = 1) in vec2 textureCoordinate;

out vec2 fragTextureCoordinate;

void main() {
    float alpha = aliveSeconds * rotationSpeed;
    vec2 offset = mat2x2(cos(alpha), sin(alpha), -sin(alpha), cos(alpha)) * positionOffset;
    vec2 movedPosition = position + velocity * aliveSeconds - vec2(0, gravity * aliveSeconds * aliveSeconds);

    gl_Position = vec4(movedPosition + offset * size, 0.5, 0.5);
    fragTextureCoordinate = textureCoordinate;
}
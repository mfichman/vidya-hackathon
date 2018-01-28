#version 330

out vec4 color;

layout(std140) uniform materialBlock {
    vec3 diffuse;
};

void main() {
    //color = vec4(1.0, 0, 0, 1.0);
    color = vec4(diffuse, 1.0);
}
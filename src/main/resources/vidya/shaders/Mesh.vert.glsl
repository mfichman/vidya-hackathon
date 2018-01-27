#version 330
#extension GL_GOOGLE_cpp_style_line_directive : enable

layout(location = 0) in vec3 position;

layout(std140) uniform transformBlock {
    mat4 worldViewProjMatrix;
};

/* Very fast simple solid-color shader for rendering to depth */
void main() {
	// Transform the vertex to get the clip-space position of the vertex
    gl_Position = worldViewProjMatrix * vec4(position, 1);
}
package talantra.game;

public class VertexData{
	// The amount of bytes an element has
	public static final int elementBytes = 4;
	// Elements per parameter
	public static final int positionElementCount = 4;
	public static final int colorElementCount = 4;
	public static final int textureElementCount = 2;
	// Bytes per parameter
	public static final int positionBytesCount = positionElementCount*elementBytes;
	public static final int colorByteCount = colorElementCount*elementBytes;
	public static final int textureByteCount = textureElementCount*elementBytes;
	// Byte offsets per parameter
	public static final int positionByteOffset = 0;
	public static final int colorByteOffset = positionByteOffset+positionBytesCount;
	public static final int textureByteOffset = colorByteOffset+colorByteCount;
	// The amount of elements that a vertex has
	public static final int elementCount = positionElementCount+colorElementCount+textureElementCount;
	// The size of a vertex in bytes, like in C/C++: sizeof(Vertex)
	public static final int stride = positionBytesCount+colorByteCount+textureByteCount;
	// Vertex data
	private float[] xyzw = new float[]{
		0f, 0f, 0f, 1f
	};
	private float[] rgba = new float[]{
		1f, 1f, 1f, 1f
	};
	private float[] st = new float[]{
		0f, 0f
	};
	// Getters
	public float[] getElements(){
		float[] out = new float[VertexData.elementCount];
		int i = 0;
		// Insert XYZW elements
		out[i++] = xyzw[0];
		out[i++] = xyzw[1];
		out[i++] = xyzw[2];
		out[i++] = xyzw[3];
		// Insert RGBA elements
		out[i++] = rgba[0];
		out[i++] = rgba[1];
		out[i++] = rgba[2];
		out[i++] = rgba[3];
		// Insert ST elements
		out[i++] = st[0];
		out[i++] = st[1];
		return out;
	}
	public float[] getRGB(){
		return new float[]{
			rgba[0], rgba[1], rgba[2]
		};
	}
	public float[] getRGBA(){
		return new float[]{
			rgba[0], rgba[1], rgba[2], rgba[3]
		};
	}
	public float[] getST(){
		return new float[]{
			st[0], st[1]
		};
	}
	public float[] getXYZ(){
		return new float[]{
			xyzw[0], xyzw[1], xyzw[2]
		};
	}
	public float[] getXYZW(){
		return new float[]{
			xyzw[0], xyzw[1], xyzw[2], xyzw[3]
		};
	}
	public void setRGB(float r, float g, float b){
		setRGBA(r, g, b, 1f);
	}
	public void setRGBA(float r, float g, float b, float a){
		rgba = new float[]{
			r, g, b, a
		};
	}
	public void setST(float s, float t){
		st = new float[]{
			s, t
		};
	}
	// Setters
	public void setXYZ(float x, float y, float z){
		setXYZW(x, y, z, 1f);
	}
	public void setXYZW(float x, float y, float z, float w){
		xyzw = new float[]{
			x, y, z, w
		};
	}
}

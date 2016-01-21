package talantra.game;

import java.util.Arrays;

public class PrimitiveGenerator{
	public static class PrimitiveFlags{
		private final boolean position;
		private final boolean texture;
		public PrimitiveFlags(boolean position, boolean texture){
			this.position = position;
			this.texture = texture;
		}
	}
	private static class Builder{
		private final PrimitiveFlags flags;
		private final int vertDataSize;
		private float[] verts = new float[0];
		private short[] indes = new short[0];
		private Builder(PrimitiveFlags flags){
			this.flags = flags;
			int size = 0;
			if(flags.position)
				size += 3;
			if(flags.texture)
				size += 2;
			vertDataSize = size;
		}
		private void add(float x, float y, float z, float s, float t){
			addIndex(addVertex(x, y, z, s, t));
		}
		private void addIndex(int index){
			indes = Arrays.copyOf(indes, indes.length+1);
			indes[indes.length-1] = (short)index;
		}
		private int addVertex(float x, float y, float z, float s, float t){
			int offset;
			for(int i = 0; i<verts.length; i += vertDataSize){
				offset = 0;
				if(flags.position){
					if(verts[i+offset++]!=x)
						continue;
					if(verts[i+offset++]!=y)
						continue;
					if(verts[i+offset++]!=z)
						continue;
				}
				if(flags.texture){
					if(verts[i+offset++]!=s)
						continue;
					if(verts[i+offset++]!=t)
						continue;
				}
				return i/vertDataSize;
			}
			int pos = verts.length;
			verts = Arrays.copyOf(verts, verts.length+vertDataSize);
			if(flags.position){
				verts[pos++] = x;
				verts[pos++] = y;
				verts[pos++] = z;
			}
			if(flags.texture){
				verts[pos++] = s;
				verts[pos++] = t;
			}
			return pos/vertDataSize-1;
		}
	}
	public static VertexBuildData generateBox(float x, float y, float z, PrimitiveFlags flags){
		Builder builder = new Builder(flags);
		builder.add(-x, -y, -z, 0, 0);
		builder.add(x, -y, z, 1, 1);
		builder.add(-x, -y, z, 1, 0);
		builder.add(-x, -y, -z, 0, 0);
		builder.add(x, -y, -z, 0, 1);
		builder.add(x, -y, z, 1, 1);
		System.out.println("Generated box, ["+x*2+"x"+y*2+"x"+z*2+"] V:"+builder.verts.length+" I:"+builder.indes.length);
		// builder.place(-radiusX, -radiusY, -radiusZ, 0, 0);
		// builder.place(-radiusX, -radiusY, radiusZ, 0, 0);
		// builder.place(-radiusX, radiusY, -radiusZ, 0, 0);
		// builder.place(-radiusX, radiusY, radiusZ, 0, 0);
		// builder.place(radiusX, -radiusY, -radiusZ, 0, 0);
		// builder.place(radiusX, -radiusY, radiusZ, 0, 0);
		// builder.place(radiusX, radiusY, -radiusZ, 0, 0);
		// builder.place(radiusX, radiusY, radiusZ, 1, 1);
		// builder.placeTri(0, 5, 1);
		// builder.placeTri(0, 4, 5);
		// builder.placeTri(0, 2, 6);
		// builder.placeTri(0, 6, 4);
		// builder.placeTri(0, 1, 2);
		// builder.placeTri(1, 3, 2);
		// builder.placeTri(1, 7, 3);
		// builder.placeTri(1, 5, 7);
		// builder.placeTri(4, 6, 5);
		// builder.placeTri(5, 6, 7);
		// builder.placeTri(2, 3, 7);
		// builder.placeTri(2, 7, 6);
		return new VertexBuildData(builder.verts, builder.indes);
	}
	public static PrimitiveFlags POSITION_ONLY = new PrimitiveFlags(true, false);
	public static PrimitiveFlags ALL = new PrimitiveFlags(true, true);
}

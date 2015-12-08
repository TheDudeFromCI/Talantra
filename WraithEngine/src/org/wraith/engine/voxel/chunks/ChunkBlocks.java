package org.wraith.engine.voxel.chunks;

public interface ChunkBlocks{
	public Block getBlock(int x, int y, int z);
	public int getChunkX();
	public int getChunkY();
	public int getChunkZ();
	public boolean isOld();
	public boolean needsRemesh();
	public void rebuildMesh(ChunkMesh mesh);
	public void save();
	public void setBlock(int x, int y, int z, Block block);
}

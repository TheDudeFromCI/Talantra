package org.wraith.engine.voxel.chunks;

import java.util.ArrayList;

public interface ChunkLoader{
	public int getChunkSize();
	public ChunkBlocks loadChunkBlocks(int x, int y, int z);
	public ChunkMesh loadChunkMesh(int x, int y, int z);
	public ChunkMesh loadNextChunkMesh(ArrayList<ChunkMesh> loadedMeshes);
}

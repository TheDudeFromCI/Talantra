package org.wraith.engine.voxel.chunks;

import java.util.ArrayList;
import org.wraith.engine.util.Algorithms;

public class World{
	private final ArrayList<ChunkMesh> meshes = new ArrayList();
	private final ArrayList<ChunkBlocks> blocks = new ArrayList();
	private final ChunkLoader chunkLoader;
	private final ChunkRenderer chunkRenderer;
	public World(ChunkLoader chunkLoader, ChunkRenderer chunkRenderer){
		this.chunkLoader = chunkLoader;
		this.chunkRenderer = chunkRenderer;
	}
	public Block getBlock(int x, int y, int z){
		int chunkSize = chunkLoader.getChunkSize();
		int chunkX = Algorithms.groupLocation(x, chunkSize);
		int chunkY = Algorithms.groupLocation(y, chunkSize);
		int chunkZ = Algorithms.groupLocation(z, chunkSize);
		return getChunkBlocks(chunkX, chunkY, chunkZ).getBlock(x, y, z);
	}
	public ChunkBlocks getChunkBlocks(int x, int y, int z){
		for(ChunkBlocks c : blocks)
			if(c.getChunkX()==x&&c.getChunkY()==y&&c.getChunkZ()==z)
				return c;
		ChunkBlocks c = chunkLoader.loadChunkBlocks(x, y, z);
		blocks.add(c);
		return c;
	}
	public ChunkMesh getChunkMesh(int x, int y, int z){
		for(ChunkMesh c : meshes)
			if(c.getChunkX()==x&&c.getChunkY()==y&&c.getChunkZ()==z)
				return c;
		ChunkMesh c = chunkLoader.loadChunkMesh(x, y, z);
		meshes.add(c);
		return c;
	}
	public void render(){
		chunkRenderer.render(meshes);
	}
	public void setBlock(int x, int y, int z, Block block){
		int chunkSize = chunkLoader.getChunkSize();
		int chunkX = Algorithms.groupLocation(x, chunkSize);
		int chunkY = Algorithms.groupLocation(y, chunkSize);
		int chunkZ = Algorithms.groupLocation(z, chunkSize);
		ChunkBlocks chunk = getChunkBlocks(chunkX, chunkY, chunkZ);
		chunk.setBlock(x, y, z, block);
	}
	public void update(){
		meshes.add(chunkLoader.loadNextChunkMesh(meshes));
		for(ChunkBlocks c : blocks)
			if(c.needsRemesh())
				c.rebuildMesh(getChunkMesh(c.getChunkX(), c.getChunkY(), c.getChunkZ()));
		for(int i = 0; i<blocks.size();)
			if(blocks.get(i).isOld())
				blocks.remove(i);
			else
				i++;
	}
}

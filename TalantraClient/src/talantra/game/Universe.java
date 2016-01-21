package talantra.game;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

public class Universe{
	private final FloatBuffer buffer;
	private final ArrayList<Model> models = new ArrayList();
	private final UniverseFlags flags;
	private ShaderProgram shader;
	private Camera camera;
	private int projectionLocation;
	private int viewLocation;
	private int modelLocation;
	public Universe(){
		buffer = BufferUtils.createFloatBuffer(16);
		flags = new UniverseFlags();
	}
	public void addModel(Model model){
		models.add(model);
	}
	public void dispose(){
		for(Model model : models)
			model.dispose();
		models.clear();
		shader = null;
		camera = null;
	}
	public UniverseFlags getFlags(){
		return flags;
	}
	public void removeModel(Model model){
		models.remove(model);
	}
	public void render(){
		if(shader==null)
			return;
		if(models.isEmpty())
			return;
		if(camera==null)
			return;
		shader.bind();
		flags.bind();
		camera.dumpToShader(viewLocation, projectionLocation);
		for(Model model : models){
			model.getMatrix().store(buffer);
			buffer.flip();
			GL20.glUniformMatrix4(modelLocation, false, buffer);
			model.render();
		}
		flags.unbind();
		shader.unbind();
	}
	public void setCamera(Camera camera){
		this.camera = camera;
	}
	public void setShader(ShaderProgram shader, int projectionLocation, int viewLocation, int modelLocation){
		this.shader = shader;
		this.projectionLocation = shader.getUniformLocations()[projectionLocation];
		this.viewLocation = shader.getUniformLocations()[viewLocation];
		this.modelLocation = shader.getUniformLocations()[modelLocation];
	}
	public void update(double delta, double time){
		for(int i = 0; i<models.size(); i++)
			models.get(i).update(delta, time);
	}
}

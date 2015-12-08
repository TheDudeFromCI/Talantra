package org.wraith.engine.rendering;

import java.util.ArrayList;
import java.util.Collections;

public class Universe{
	private final ArrayList<Model> models = new ArrayList();
	public void addModel(Model model){
		models.add(model);
	}
	public void dispose(){
		for(Model model : models)
			model.dispose();
		models.clear();
	}
	public void removeModel(Model model){
		models.remove(model);
	}
	public void render(){
		// Render all models by their required shader.
		ShaderProtocol shader = null;
		for(Model model : models){
			// Don't render anything that doesn't have a shader attached.
			if(model.getShader()==null)
				continue;
			if(model.getShader()!=shader){
				if(shader!=null)
					shader.unbind();
				shader = model.getShader();
				shader.bind();
			}
			model.render();
		}
		// And clean up. ^^
		if(shader!=null)
			shader.unbind();
	}
	public void sortModels(){
		Collections.sort(models);
	}
	public void update(){
		for(int i = 0; i<models.size(); i++)
			models.get(i).update();
	}
}

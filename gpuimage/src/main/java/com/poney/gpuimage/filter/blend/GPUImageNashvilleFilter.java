package com.poney.gpuimage.filter.blend;

import android.opengl.GLES20;

import com.poney.gpuimage.R;
import com.poney.gpuimage.filter.base.GPUImageBlendFilter;
import com.poney.gpuimage.filter.base.GPUImageParams;
import com.poney.gpuimage.utils.OpenGlUtils;

public class GPUImageNashvilleFilter extends GPUImageBlendFilter {
	private int[] inputTextureHandles = {-1};
	private int[] inputTextureUniformLocations = {-1};
    private int mGLStrengthLocation;

	public GPUImageNashvilleFilter(){
		super(NO_FILTER_VERTEX_SHADER, OpenGlUtils.readShaderFromRawResource(R.raw.nashville));
	}
	
	public void onDestroy() {
        super.onDestroy();
        GLES20.glDeleteTextures(1, inputTextureHandles, 0);
        for(int i = 0; i < inputTextureHandles.length; i++)
        	inputTextureHandles[i] = -1;
    }
	
	public void onDrawArraysAfter(){
		for(int i = 0; i < inputTextureHandles.length
				&& inputTextureHandles[i] != OpenGlUtils.NO_TEXTURE; i++){
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + (i+3));
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		}
	}
	  
	public void onDrawArraysPre(){
		for(int i = 0; i < inputTextureHandles.length 
				&& inputTextureHandles[i] != OpenGlUtils.NO_TEXTURE; i++){
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + (i+3) );
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, inputTextureHandles[i]);
			GLES20.glUniform1i(inputTextureUniformLocations[i], (i+3));
		}
	}
	
	public void onInit(){
		super.onInit();
		for(int i=0; i < inputTextureUniformLocations.length; i++)
			inputTextureUniformLocations[i] = GLES20.glGetUniformLocation(getProgram(), "inputImageTexture"+(2+i));
		mGLStrengthLocation = GLES20.glGetUniformLocation(getProgram(),
				"strength");
	}
	
	public void onInitialized(){
		super.onInitialized();
		setFloat(mGLStrengthLocation, 1.0f);
	    runOnDraw(new Runnable(){
		    public void run(){
		    	inputTextureHandles[0] = OpenGlUtils.loadTexture(GPUImageParams.sContext, "filter/nashvillemap.png");
		    }
	    });
	}
}
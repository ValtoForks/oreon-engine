package org.oreon.modules.gl.terrain.fractals;

import org.oreon.core.gl.texture.Texture2D;
import org.oreon.core.math.Vec2f;

public class FractalMap {
	
	private int N;
	private float amplitude; 
	private float l;
	private Texture2D heightmap;
	private int scaling;
	private float strength;
	private int random;
	
	public FractalMap(int N, float amplitude, float l, int scaling, float strength, int random){
		
		this.scaling = scaling;
		this.strength = strength;
		this.setAmplitude(amplitude);
		this.random = random;
		this.l = l;
		this.N = N;
		int L = 1000;
		int v = 100;
		this.scaling = scaling;
		this.strength = strength;
		Vec2f w = new Vec2f(1,3).normalize();
		FractalFFT fft = new FractalFFT(N,L,amplitude,v,w,l);
		fft.setT(random);
		fft.init();
		fft.render();
		heightmap = fft.getHeightmap();
	}

	public Texture2D getHeightmap() {
		return heightmap;
	}

	public void setHeightmap(Texture2D heightmap) {
		this.heightmap = heightmap;
	}

	public int getScaling() {
		return scaling;
	}

	public float getStrength() {
		return strength;
	}
	
	public void setStrength(float s) {
		strength = s;
	}

	public int getRandom() {
		return random;
	}

	public void setRandom(int random) {
		this.random = random;
	}

	public float getAmplitude() {
		return amplitude;
	}

	public void setAmplitude(float amplitude) {
		this.amplitude = amplitude;
	}

	public float getL() {
		return l;
	}

	public int getN() {
		return N;
	}
}

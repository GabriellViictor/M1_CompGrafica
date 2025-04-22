package core3d;

import java.awt.Graphics2D;

public class Triangulo3D {
	Ponto3D pa;
	Ponto3D pb;
	Ponto3D pc;

	public Triangulo3D(Ponto3D a, Ponto3D b, Ponto3D c) {
		super();
		this.pa = new Ponto3D(a);
		this.pb = new Ponto3D(b);
		this.pc = new Ponto3D(c);
	}

	public Triangulo3D(Triangulo3D tri){
		this.pa = new Ponto3D(tri.pa);
		this.pb = new Ponto3D(tri.pb);
		this.pc = new Ponto3D(tri.pc);
	}

	public void desenhase(Graphics2D dbg) {
		dbg.drawLine((int)pa.x,(int)pa.y,(int)pb.x,(int)pb.y);
		dbg.drawLine((int)pb.x,(int)pb.y,(int)pc.x,(int)pc.y);
		dbg.drawLine((int)pc.x,(int)pc.y,(int)pa.x,(int)pa.y);
	}

	public Ponto3D getPa() {
		return this.pa;
	}
	public Ponto3D getPb() {
		return pb;
	}
	public Ponto3D getPc() {
		return pc;
	}

	public void desenhase(Triangulo3D tri, Graphics2D dbg,Mat4x4 vew,Mat4x4 projection) {
		Ponto3D pa2 = vew.multiplicaVetor(pa);
		Ponto3D pb2 = vew.multiplicaVetor(pb);
		Ponto3D pc2 = vew.multiplicaVetor(pc);

		pa2 = projection.multiplicaVetor(pa2);
		pb2 = projection.multiplicaVetor(pb2);
		pc2 = projection.multiplicaVetor(pc2);

		pa2.normalizaW();
		pb2.normalizaW();
		pc2.normalizaW();

		pa2.x+=400;
		pb2.x+=400;
		pc2.x+=400;

		pa2.y+=325;
		pb2.y+=325;
		pc2.y+=325;

		dbg.drawLine((int)pa2.x,(int)pa2.y,(int)pb2.x,(int)pb2.y);
		dbg.drawLine((int)pb2.x,(int)pb2.y,(int)pc2.x,(int)pc2.y);
		dbg.drawLine((int)pc2.x,(int)pc2.y,(int)pa2.x,(int)pa2.y);
	}
	/*
	public void translacao(float a,float b, float c) {
		Mat4x4 m = new Mat4x4();
		m.setTranslate(a, b,c);
		pa.multiplicaMat(m);
		pb.multiplicaMat(m);
		pc.multiplicaMat(m);
	}
	public void escala(float a,float b,float c) {
		Mat4x4 m = new Mat4x4();
		m.setSacale(a, b, c);
		pa.multiplicaMat(m);
		pb.multiplicaMat(m);
		pc.multiplicaMat(m);
	}	
	
	public void rotacaoY(float ang) {
		Mat4x4 m = new Mat4x4();
		m.setRotateY(ang);
		//System.out.println("rot Y");
		pa.multiplicaMat(m);
		pb.multiplicaMat(m);
		pc.multiplicaMat(m);
	}

	public void rotacaoX(float ang) {
		Mat4x4 m = new Mat4x4();
		m.setRotateX(ang);
		//System.out.println("rot Y");
		pa.multiplicaMat(m);
		pb.multiplicaMat(m);
		pc.multiplicaMat(m);
	}

	public void rotacaoZ(float ang) {
		Mat4x4 m = new Mat4x4();
		m.setRotateZ(ang);
		//System.out.println("rot Y");
		pa.multiplicaMat(m);
		pb.multiplicaMat(m);
		pc.multiplicaMat(m);
	}

	public void anyRotation(float ang, float x, float  y, float z) {
		Mat4x4 m = new Mat4x4();
		m.setRotateAny(ang,x, y, z);
		//System.out.println("rot Y");
		pa.multiplicaMat(m);
		pb.multiplicaMat(m);
		pc.multiplicaMat(m);
	}

	public void projecoes(float ang, float x) {
		Mat4x4 m = new Mat4x4();
		m.pointView(ang,x);
		//System.out.println("rot Y");
		pa.multiplicaMat(m);
		pb.multiplicaMat(m);
		pc.multiplicaMat(m);
	}
*/

}

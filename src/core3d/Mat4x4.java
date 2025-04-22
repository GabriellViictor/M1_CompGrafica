package core3d;

public class Mat4x4 {
	double mat[][] = new double[4][4];
	
	public Mat4x4() {
		setIdentity();
	}
	public void setIdentity() {
		zera();
		mat[0][0] = 1;
		mat[1][1] = 1;
		mat[2][2] = 1;
		mat[3][3] = 1;
	}
	
	public void zera() {
		for(int y = 0; y < 4;y++) {
			for(int x = 0; x < 4;x++) {
				mat[y][x] = 0;
			}
		}
	}
	
	public void setTranslate(double a,double b,double c) {
		zera();
		mat[0][0] = 1;
		mat[1][1] = 1;
		mat[2][2] = 1;
		
		mat[0][3] = a;
		mat[1][3] = b;
		mat[2][3] = c;
		mat[3][3] = 1;
	}
	
	public void setRotateY(double ang) {
		zera();
		double rad = ang*0.017453f;
		double sin = Math.sin(rad);
		double cos = Math.cos(rad);
		mat[0][0] = cos;
		mat[0][1] = 0;
		mat[0][2] = -sin;
		mat[0][3] = 0;
		
		mat[1][0] = 0;
		mat[1][1] = 1;
		mat[1][2] = 0;
		mat[1][3] = 0;
		
		mat[2][0] = sin;
		mat[2][1] = 0;
		mat[2][2] = cos;
		mat[2][3] = 0;
		
		mat[3][0] = 0;
		mat[3][1] = 0;
		mat[3][2] = 0;
		mat[3][3] = 1;
	}


	public void setRotateX(double ang) {
		zera();
		double rad = ang*0.017453f;
		double sin = Math.sin(rad);
		double cos = Math.cos(rad);
		mat[0][0] = 1;
		mat[0][1] = 0;
		mat[0][2] = 0;
		mat[0][3] = 0;

		mat[1][0] = 0;
		mat[1][1] = cos;
		mat[1][2] = -sin;
		mat[1][3] = 0;

		mat[2][0] = 0;
		mat[2][1] = sin;
		mat[2][2] = cos;
		mat[2][3] = 0;

		mat[3][0] = 0;
		mat[3][1] = 0;
		mat[3][2] = 0;
		mat[3][3] = 1;
	}


	public void setRotateZ(double ang) {
		zera();
		double rad = ang*0.017453f;
		double sin = (float)Math.sin(rad);
		double cos = (float)Math.cos(rad);
		mat[0][0] = cos;
		mat[0][1] = -sin;
		mat[0][2] = 0;
		mat[0][3] = 0;

		mat[1][0] = sin;
		mat[1][1] = cos;
		mat[1][2] = 0;
		mat[1][3] = 0;

		mat[2][0] = 0;
		mat[2][1] = 0;
		mat[2][2] = 1;
		mat[2][3] = 0;

		mat[3][0] = 0;
		mat[3][1] = 0;
		mat[3][2] = 0;
		mat[3][3] = 1;
	}

	public void setRotateAny(double ang, double x, double y, double z) {
		zera();

		double length = Math.sqrt(x*x + y*y + z*z);

		if (length == 0) length = 1; // evita divisão por zero

		double ux = x/length;
		double uy = y/length;
		double uz = z/length;

		double rad = ang * 0.017453f;
		double cos = (float)Math.cos(rad);
		double sin = (float)Math.sin(rad);
		double oneMinusCos = 1.0f - cos;

		mat[0][0] = cos + ux*ux*oneMinusCos;
		mat[0][1] = ux*uy*oneMinusCos - uz*sin;
		mat[0][2] = ux*uz*oneMinusCos + uy*sin;
		mat[0][3] = 0;

		mat[1][0] = uy*ux*oneMinusCos + uz*sin;
		mat[1][1] = cos + uy*uy*oneMinusCos;
		mat[1][2] = uy*uz*oneMinusCos - ux*sin;
		mat[1][3] = 0;

		mat[2][0] = uz*ux*oneMinusCos - uy*sin;
		mat[2][1] = uz*uy*oneMinusCos + ux*sin;
		mat[2][2] = cos + uz*uz*oneMinusCos;
		mat[2][3] = 0;

		mat[3][0] = 0;
		mat[3][1] = 0;
		mat[3][2] = 0;
		mat[3][3] = 1;
	}

	public void pointView(double ang, double d) {
		zera();

		mat[0][0] = 1;
		mat[0][1] = 0;
		mat[0][2] = 0;
		mat[0][3] = 0;

		mat[1][0] = 0;
		mat[1][1] = 1;
		mat[1][2] = 0;
		mat[1][3] = 0;

		mat[2][0] = 0;
		mat[2][1] = 0;
		mat[2][2] = 0;
		mat[2][3] = 0;

		mat[3][0] = 0;
		mat[3][1] = 0;
		mat[3][2] = 1/d;
		mat[3][3] = 1;
	}

	public void setPerspectiva(float d) {

		zera();
		mat[0][0] = 1;
		mat[0][1] = 0;
		mat[0][2] = 0;
		mat[0][3] = 0;

		mat[1][0] = 0;
		mat[1][1] = 1;
		mat[1][2] = 0;
		mat[1][3] = 0;

		mat[2][0] = 0;
		mat[2][1] = 0;
		mat[2][2] = 0;
		mat[2][3] = 0;

		mat[3][2] = 1/d;
		mat[3][3] = 1;
	}

	public Mat4x4 multiplicaMatrix(Mat4x4 m2) {
		Mat4x4 rmat = new Mat4x4();
		for(int ym1 = 0; ym1 < 4; ym1++ ) {
			for(int xm2 = 0; xm2 < 4; xm2++ ) {;
				float total = 0;
				for(int pos = 0; pos < 4;pos++) {
					total+= mat[ym1][pos]*m2.mat[pos][xm2];
				}
				rmat.mat[ym1][xm2] = total;
			}
		}

		return rmat;
	}

	public Ponto3D multiplicaVetor(Ponto3D vet) {
		double x1 = mat[0][0]*vet.x+mat[0][1]*vet.y+mat[0][2]*vet.z+mat[0][3]*vet.w;
		double y1 = mat[1][0]*vet.x+mat[1][1]*vet.y+mat[1][2]*vet.z+mat[1][3]*vet.w;
		double z1 = mat[2][0]*vet.x+mat[2][1]*vet.y+mat[2][2]*vet.z+mat[2][3]*vet.w;
		double w1 = mat[3][0]*vet.x+mat[3][1]*vet.y+mat[3][2]*vet.z+mat[3][3]*vet.w;

		if(w1!=1) {
			x1 = x1/w1;
			y1 = y1/w1;
			z1 = z1/w1;
			w1 = 1;
		}

		return new Ponto3D((float)x1,(float)y1,(float)z1,(float)w1);
	}

	public void setScale(double a,double b,double c) {
		zera();
		mat[0][0] = a;
		mat[1][1] = b;
		mat[2][2] = c;
		mat[3][3] = 1;
	}

//	public void setRotateAxis(float ang,float x0,float y0) {
//		zera();
//		float rad = ang*0.017453f;
//		float sin = (float)Math.sin(rad);
//		float cos = (float)Math.cos(rad);
//		mat[0][0] = cos;
//		mat[0][1] = -sin;
//		mat[0][2] = x0*(1-cos)+y0*sin;
//		
//		mat[1][0] = sin;
//		mat[1][1] = cos;
//		mat[1][2] = y0*(1-cos)-x0*sin;
//		
//		mat[2][0] = 0;
//		mat[2][1] = 0;
//		mat[2][2] = 1;
//	}	
}

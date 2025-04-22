import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import core2d.Linha2D;
import core2d.Ponto2D;
import core2d.Triangulo2D;
import core3d.Ponto3D;
import core3d.Triangulo3D;
import core3d.Mat4x4;

public class MainCanvas extends JPanel implements Runnable {
	int W = 800;
	int H = 600;

	Thread runner;
	boolean ativo = true;
	int paintcounter = 0;

	BufferedImage imageBuffer;
	byte bufferDeVideo[];

	Random rand = new Random();

	byte memoriaPlacaVideo[];
	short paleta[][];

	int framecount = 0;
	int fps = 0;

	Font f = new Font("", Font.PLAIN, 30);

	int clickX = 0;
	int clickY = 0;
	int mouseX = 0;
	int mouseY = 0;

	int pixelSize = 0;
	int Largura = 0;
	int Altura = 0;

	BufferedImage imgtmp = null;

	float posx = 00;
	float posy = 00;

	boolean LEFT = false;
	boolean RIGHT = false;
	boolean UP = false;
	boolean DOWN = false;

	float filtroR = 1;
	float filtroG = 1;
	float filtroB = 1;

	int estado = 0;

	// ArrayList<Linha2D> listaDeLinhas = new ArrayList<>();
	ArrayList<Triangulo3D> listaDeTriangulos = new ArrayList<>();
	
	int eixoX = 0;
	int eixoY = 0;

	Mat4x4 viewMatrix = new Mat4x4();
	Mat4x4 projectionMatrix = new Mat4x4();

	public MainCanvas() {

		File f = new File("imgbmp.bmp");
		try {
			FileInputStream fin = new FileInputStream(f);

			byte todosodbytes[] = new byte[64000];
			int byteslidos = fin.read(todosodbytes);
			System.out.println("Bytes Lidos " + byteslidos);
			for (int i = 0; i < byteslidos; i++) {
				System.out.println(i + ": " + todosodbytes[i]);
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		setSize(800, 650);
		setFocusable(true);

		Largura = 800;
		Altura = 650;

		pixelSize = 800 * 650;

		imgtmp = loadImage("fundo.jpg");

		imageBuffer = new BufferedImage(800, 650, BufferedImage.TYPE_4BYTE_ABGR);

		bufferDeVideo = ((DataBufferByte) imageBuffer.getRaster().getDataBuffer()).getData();

		System.out.println("Buffer SIZE " + bufferDeVideo.length);

		addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_W) {
					UP = false;
				}
				if (key == KeyEvent.VK_S) {
					DOWN = false;
				}
				if (key == KeyEvent.VK_A) {
					LEFT = false;
				}
				if (key == KeyEvent.VK_D) {
					RIGHT = false;
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				// System.out.println("CLICO "+key);
				if (key == KeyEvent.VK_W) {
					UP = true;
				}
				if (key == KeyEvent.VK_S) {
					DOWN = true;
				}
				if (key == KeyEvent.VK_A) {
					LEFT = true;
				}
				if (key == KeyEvent.VK_D) {
					RIGHT = true;
				}
				if (key == KeyEvent.VK_Z) {
					//for (int i = 0; i < listaDeTriangulos.size(); i++) {
					//	Triangulo3D tri = listaDeTriangulos.get(i);
						Mat4x4 scale = new Mat4x4();
						scale.setScale(0.8f, 0.8f,0.8f);
						Mat4x4 m = viewMatrix.multiplicaMatrix(scale);
						viewMatrix = m;
						//tri.escala(0.8f, 0.8f,0.8f);
					//}
				}
				if (key == KeyEvent.VK_X) {
					//for (int i = 0; i < listaDeTriangulos.size(); i++) {
					//	Triangulo3D tri = listaDeTriangulos.get(i);
						Mat4x4 scale = new Mat4x4();
						scale.setScale(1.2f, 1.2f,1.2f);
						Mat4x4 m = viewMatrix.multiplicaMatrix(scale);
						viewMatrix = m;
						//tri.escala(1.2f, 1.2f, 1.2f);
					//}
				}
				if (key == KeyEvent.VK_Q) {
					Mat4x4 rot = new Mat4x4();
					rot.setRotateY(-5);
					Mat4x4 m = viewMatrix.multiplicaMatrix(rot);
					viewMatrix = m;
				}
				if (key == KeyEvent.VK_E) {
					Mat4x4 rot = new Mat4x4();
					rot.setRotateY(+5);
					Mat4x4 m = viewMatrix.multiplicaMatrix(rot);
					viewMatrix = m;
				}

				if (key == KeyEvent.VK_J) {
					/*for (int i = 0; i < listaDeTriangulos.size(); i++) {
						Triangulo3D tri = listaDeTriangulos.get(i);
						tri.anyRotation(+5, 5.0f,5.0f,5.0f);
					}*/
					Mat4x4 rot = new Mat4x4();
					rot.setRotateAny(5, 7, 14, 18);
					Mat4x4 m = viewMatrix.multiplicaMatrix(rot);
					viewMatrix = m;
				}

				if (key == KeyEvent.VK_T) {
					/*for (int i = 0; i < listaDeTriangulos.size(); i++) {
						Triangulo3D tri = listaDeTriangulos.get(i);
						tri.projecoes(+5, 5.0f);
					}
					*/
				}

			}
		});


		addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				clickX = e.getX();
				clickY = e.getY();

				if(e.getButton()==3) {
					eixoX = clickX;
					eixoY = clickY;
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});

		//Triangulo3D tri = new Triangulo3D(new Ponto3D(0, 0, 0), new Ponto3D(200, 0, 0), new Ponto3D(0, 200, 0));
		//criaCubo(100,100,0,100,100,100);
		//criaCubo(300,200,0,100,200,100);

		carregarAK47("src/lowpolycat/cat.obj");

		viewMatrix.setIdentity();
		projectionMatrix.setIdentity();
		projectionMatrix.setPerspectiva(600);
	}

	private void carregarAK47(String caminho) {
		ArrayList<Ponto3D> vertices = new ArrayList<>();

		try {
			FileInputStream fis = new FileInputStream(caminho);
			DataInputStream dis = new DataInputStream(fis);
			String linha;

			while ((linha = dis.readLine()) != null) {
				if (linha.startsWith("v ")) {
					// Linha de vértice
					String[] partes = linha.split("\\s+");
					float x = Float.parseFloat(partes[1]);
					float y = Float.parseFloat(partes[2]);
					float z = Float.parseFloat(partes[3]);
					vertices.add(new Ponto3D(x, y, z));
				} else if (linha.startsWith("f ")) {
					// Linha de face
					String[] partes = linha.split("\\s+");
					int v1 = Integer.parseInt(partes[1].split("/")[0]) - 1;
					int v2 = Integer.parseInt(partes[2].split("/")[0]) - 1;
					int v3 = Integer.parseInt(partes[3].split("/")[0]) - 1;

					Triangulo3D triangulo = new Triangulo3D(vertices.get(v1), vertices.get(v2), vertices.get(v3));
					listaDeTriangulos.add(triangulo);
				}
			}

			dis.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void criaCubo(float x,float y, float z, float lx,float ly, float lz) {
		Ponto3D p1 = new Ponto3D(x, y, z);
		Ponto3D p2 = new Ponto3D(x+lx, y, z);
		Ponto3D p3 = new Ponto3D(x+lx, y+ly, z);
		Ponto3D p4 = new Ponto3D(x, y+ly, z);
		
		Ponto3D p5 = new Ponto3D(x, y, z+lz);
		Ponto3D p6 = new Ponto3D(x+lx, y, z+lz);
		Ponto3D p7 = new Ponto3D(x+lx, y+ly, z+lz);
		Ponto3D p8 = new Ponto3D(x, y+ly, z+lz);
		
		listaDeTriangulos.add(new Triangulo3D(p1,p2,p3));
		listaDeTriangulos.add(new Triangulo3D(p3,p4,p1));
		
		listaDeTriangulos.add(new Triangulo3D(p5,p6,p7));
		listaDeTriangulos.add(new Triangulo3D(p7,p8,p5));
		
		listaDeTriangulos.add(new Triangulo3D(p1,p4,p5));
		listaDeTriangulos.add(new Triangulo3D(p4,p8,p5));
		
		listaDeTriangulos.add(new Triangulo3D(p2,p3,p6));
		listaDeTriangulos.add(new Triangulo3D(p3,p7,p6));
		
		listaDeTriangulos.add(new Triangulo3D(p1,p2,p6));
		listaDeTriangulos.add(new Triangulo3D(p1,p6,p5));
		
		listaDeTriangulos.add(new Triangulo3D(p4,p3,p7));
		listaDeTriangulos.add(new Triangulo3D(p6,p7,p8));
	}

	private void drawImageToBuffer(BufferedImage image, int x, int y, float fr, float fg, float fb) {
		byte[] imgBuffer = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();

		int iw = image.getWidth();
		int ih = image.getHeight();

		for (int yi = 0; yi < ih; yi++) {
			for (int xi = 0; xi < iw; xi++) {
				int pixi = yi * iw * 4 + xi * 4;
				int pixb = (yi + y) * W * 4 + (xi + x) * 4;
				bufferDeVideo[pixb] = imgBuffer[pixi];

				int b = (imgBuffer[pixi + 1] & 0xff);
				int g = (imgBuffer[pixi + 2] & 0xff);
				int r = (imgBuffer[pixi + 3] & 0xff);

				b = (int) (b * fb);
				g = (int) (g * fg);
				r = (int) (r * fr);

				b = Math.min(255, b);
				g = Math.min(255, g);
				r = Math.min(255, r);

				bufferDeVideo[pixb + 1] = (byte) (b & 0xff);
				bufferDeVideo[pixb + 2] = (byte) (g & 0xff);
				bufferDeVideo[pixb + 3] = (byte) (r & 0xff);
			}
		}
	}

	@Override
	public void paint(Graphics g) {
		limparBuffer();
//		for(int i = 0; i < bufferDeVideo.length; i++) {
//			bufferDeVideo[i] = 0;
//		}

		g.setFont(f);

		g.setColor(Color.white);
		g.fillRect(0, 0, 800, 600);
		
		g.setColor(Color.blue);
		g.fillRect(eixoX-2, eixoY-2, 5, 5);

		g.setColor(Color.green);
		g.drawRect(50, 50, 700, 550);

//		g.setColor(Color.green);
//		g.drawRect(50, 50, 700, 500);

//		g.setColor(Color.black);
//		for(int i = 0; i < listaDeLinhas.size();i++) {
//			listaDeLinhas.get(i).desenhase((Graphics2D)g);
//		}

		g.setColor(Color.black);



		for (int i = 0; i < listaDeTriangulos.size(); i++) {
			Triangulo3D tri = listaDeTriangulos.get(i);
			desenhase(tri,(Graphics2D) g,viewMatrix, projectionMatrix);
		}

		g.drawImage(imageBuffer, 0, 0, null);

		g.setColor(Color.black);
		g.drawString("FPS " + fps, 10, 25);
	}

	public void desenhaLinhaHorizontal(int x, int y, int w) {
		int pospix = y * (W * 4) + x * 4;

		for (int i = 0; i < w; i++) {

			bufferDeVideo[pospix] = (byte) 255;
			bufferDeVideo[pospix + 1] = (byte) 0;
			bufferDeVideo[pospix + 2] = (byte) 0;
			bufferDeVideo[pospix + 3] = (byte) 0;
			pospix += 4;
		}
	}

	public void desenhaLinhaVertical(int x, int y, int h) {
		int pospix = y * (W * 4) + x * 4;

		for (int i = 0; i < h; i++) {

			bufferDeVideo[pospix] = (byte) 255;
			bufferDeVideo[pospix + 1] = (byte) 0;
			bufferDeVideo[pospix + 2] = (byte) 0;
			bufferDeVideo[pospix + 3] = (byte) 255;
			pospix += (W * 4);
		}
	}

	private void limparBuffer() {
		for (int i = 0; i < bufferDeVideo.length; i += 4) {
			bufferDeVideo[i] = (byte) 0;
			bufferDeVideo[i + 1] = (byte) 255;
			bufferDeVideo[i + 2] = (byte) 255;
			bufferDeVideo[i + 3] = (byte) 255;
		}
	}

	public void bresenham(int x1, int y1, int x2, int y2) {
		int dx = Math.abs(x2 - x1);
		int dy = Math.abs(y2 - y1);

		int passo_x = (x1 < x2) ? 1 : -1;
		int passo_y = (y1 < y2) ? 1 : -1;

		int erro = dx - dy;

		while (true) {
			//System.out.println("(" + x1 + ", " + y1 + ")");
			desenhaPixel(x1, y1, 255, 0, 0);
			if (x1 == x2 && y1 == y2)
				break;

			int erro2 = 2 * erro;

			if (erro2 > -dy) {
				erro -= dy;
				x1 += passo_x;
			}
			if (erro2 < dx) {
				erro += dx;
				y1 += passo_y;
			}
		}
	}

	public void desenhaPixel(int x, int y, int r, int g, int b) {
		if (x < 0 || x >= W || y < 0 || y >= H) {
			return; // Ignora pixels fora da tela
		}

		int pospix = y * (W * 4) + x * 4;

		if (pospix < 0 || pospix + 3 >= bufferDeVideo.length) {
			return; // Segurança extra caso o cálculo ainda estoure
		}

		bufferDeVideo[pospix]     = (byte) 255;
		bufferDeVideo[pospix + 1] = (byte) (b & 0xff);
		bufferDeVideo[pospix + 2] = (byte) (g & 0xff);
		bufferDeVideo[pospix + 3] = (byte) (r & 0xff);
	}


	public void start() {
		runner = new Thread(this);
		runner.start();
	}

	int timer = 0;

	public void simulaMundo(long diftime) {

		float difS = diftime / 1000.0f;
		float vel = 50;

		timer += diftime;
		if (timer >= 1000) {
			timer = 0;
			filtroR = rand.nextFloat();
			filtroG = rand.nextFloat();
			filtroB = rand.nextFloat();
		}

		//for (int i = 0; i < listaDeTriangulos.size(); i++) {
		//	Triangulo3D tri = listaDeTriangulos.get(i);
			Mat4x4 trans = new Mat4x4();

			if (UP) {
				trans.setTranslate(0, -0.1,0);
			}
			if (DOWN) {
				trans.setTranslate(0, +0.1,0);
			}
			if (LEFT) {
				trans.setTranslate(-0.1, 0,0);
			}
			if (RIGHT) {
				trans.setTranslate(+0.1, 0,0);
			}
			Mat4x4 m = viewMatrix.multiplicaMatrix(trans);
			viewMatrix = m;
		//}

	}


	private boolean clipLinha(Linha2D linha) {
		final int INSIDE = 0; // 0000
		final int LEFT = 1;   // 0001
		final int RIGHT = 2;  // 0010
		final int BOTTOM = 4; // 0100
		final int TOP = 8;    // 1000

		// Limites do retângulo de clipping (sua tela)
		float xmin = 50, ymin = 50, xmax = 750, ymax = 600;

		// Pega os pontos da linha
		float x0 = linha.a.x;
		float y0 = linha.a.y;
		float x1 = linha.b.x;
		float y1 = linha.b.y;



		// Função interna para calcular o código da região
		java.util.function.BiFunction<Float, Float, Integer> computeOutCode = (x, y) -> {
			int code = INSIDE;
			if (x < xmin) code |= LEFT;
			else if (x > xmax) code |= RIGHT;
			if (y < ymin) code |= BOTTOM;
			else if (y > ymax) code |= TOP;
			return code;
		};

		int outcode0 = computeOutCode.apply(x0, y0);
		int outcode1 = computeOutCode.apply(x1, y1);

		boolean accept = false;

		while (true) {
			if ((outcode0 | outcode1) == 0) {
				// Ambas as extremidades dentro da janela
				accept = true;
				break;
			} else if ((outcode0 & outcode1) != 0) {
				// Ambas fora da mesma região: rejeita
				break;
			} else {
				//break;

				// Pelo menos uma extremidade está fora
				float x = 0, y = 0;
				int outcodeOut = (outcode0 != 0) ? outcode0 : outcode1;

				if ((outcodeOut & TOP) != 0) {
					x = x0 + (x1 - x0) * (ymax - y0) / (y1 - y0);
					y = ymax;
				} else if ((outcodeOut & BOTTOM) != 0) {
					x = x0 + (x1 - x0) * (ymin - y0) / (y1 - y0);
					y = ymin;
				} else if ((outcodeOut & RIGHT) != 0) {
					y = y0 + (y1 - y0) * (xmax - x0) / (x1 - x0);
					x = xmax;
				} else if ((outcodeOut & LEFT) != 0) {
					y = y0 + (y1 - y0) * (xmin - x0) / (x1 - x0);
					x = xmin;
				}

				if (outcodeOut == outcode0) {
					x0 = x;
					y0 = y;
					outcode0 = computeOutCode.apply(x0, y0);
				} else {
					x1 = x;
					y1 = y;
					outcode1 = computeOutCode.apply(x1, y1);
				}
			}
		}

		if (accept) {
			linha.a.x = x0;
			linha.a.y = y0;
			linha.b.x = x1;
			linha.b.y = y1;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void run() {
		long time = System.currentTimeMillis();
		long segundo = time / 1000;
		long diftime = 0;
		while (ativo) {
			simulaMundo(diftime);
			paintImmediately(0, 0, 800, 600);
			paintcounter += 100;

			try {
				Thread.sleep(0);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			long newtime = System.currentTimeMillis();
			long novoSegundo = newtime / 1000;
			diftime = System.currentTimeMillis() - time;
			time = System.currentTimeMillis();
			framecount++;
			if (novoSegundo != segundo) {
				fps = framecount;
				framecount = 0;
				segundo = novoSegundo;
			}
		}
	}

	public BufferedImage loadImage(String filename) {
		try {
			imgtmp = ImageIO.read(new File(filename));

			BufferedImage imgout = new BufferedImage(imgtmp.getWidth(), imgtmp.getHeight(),
					BufferedImage.TYPE_4BYTE_ABGR);

			imgout.getGraphics().drawImage(imgtmp, 0, 0, null);

			imgtmp = null;

			return imgout;
		} catch (IOException e1) {
			e1.printStackTrace();
			return null;
		}
	}

	public void desenhase(Triangulo3D tri, Graphics2D dbg,Mat4x4 vew,Mat4x4 projection) {
		Ponto3D pa2 = vew.multiplicaVetor(tri.getPa());
		Ponto3D pb2 = vew.multiplicaVetor(tri.getPb());
		Ponto3D pc2 = vew.multiplicaVetor(tri.getPc());

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
/*
		dbg.drawLine();
*/
		Ponto2D Pa= new Ponto2D(pa2.x, pa2.y);
		Ponto2D Pb= new Ponto2D(pb2.x, pb2.y);
		Ponto2D Pc= new Ponto2D(pc2.x, pc2.y);

		Linha2D l1 = new Linha2D(Pa, Pb);
		Linha2D l2 = new Linha2D(Pb, Pc);
		Linha2D l3 = new Linha2D(Pc, Pa);
		Linha2D[] lados = {l1, l2, l3};

		for (Linha2D l : lados) {
			Linha2D lClip = new Linha2D(l.a, l.b); // cópia da linha
			if (clipLinha(lClip)) {
				bresenham((int) lClip.a.x, (int) lClip.a.y, (int) lClip.b.x, (int) lClip.b.y);
			}
		}
	}
}

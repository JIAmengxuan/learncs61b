public class NBody{
	
	public static double readRadius(String filename){
		In in = new In(filename);
		int numOfPlanets = in.readInt();
		double radius = in.readDouble();
		return radius;
	}

	public static Planet[] readPlanets(String filename){
		In in = new In(filename);
		int numOfPlanets = in.readInt();
		double radius = in.readDouble();
		Planet[] p = new Planet[numOfPlanets] ;
		int i = 0;//the subtitle of planets
		while(!in.isEmpty()){
			p[i] = new Planet();
			p[i].xxPos = in.readDouble();
			p[i].yyPos = in.readDouble();
			p[i].xxVel = in.readDouble();
			p[i].yyVel = in.readDouble();
			p[i].mass = in.readDouble();
			p[i].imgFileName = in.readString();	
			i++;
		}
		return p;
	}

	public static void main(String[] args){
		//StdDraw.setCanvasSize(1000,1000);
		double T = Double.parseDouble(args[0]);
		double dt = Double.parseDouble(args[1]);
		String filename = args[2];
		double radius = NBody.readRadius(filename);
		Planet[] p = NBody.readPlanets(filename);
		StdDraw.setScale(-radius, radius);
		StdDraw.enableDoubleBuffering();

		int t = 0;
		while(t <= T){
			double[] xForces = new double[p.length];
			double[] yForces = new double[p.length];
			for(int i = 0; i < p.length; i++){
				xForces[i] = p[i].calcNetForceExertedByX(p);
				yForces[i] = p[i].calcNetForceExertedByY(p);
			}
			for(int i = 0; i < p.length; i++){
				p[i].update(dt, xForces[i], yForces[i]);
			}

			StdDraw.clear();
			String background = "images/starfield.jpg";
			StdDraw.picture(0, 0, background, 2*radius, 2*radius);
			for(int i = 0; i < p.length; i++){
				p[i].draw();
			}
			StdDraw.show();
			StdDraw.pause(10);
			t += dt;
		}
		//StdAudio.play("audio/2001.mid");/*uncompleted function*/
		StdOut.printf("%d\n", p.length);
		StdOut.printf("%.2e\n", radius);
		for (int i = 0; i < p.length; i++) {
			StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
   			p[i].xxPos, p[i].yyPos, p[i].xxVel, p[i].yyVel, p[i].mass, p[i].imgFileName);	
		}	
	}
}
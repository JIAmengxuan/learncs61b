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
}
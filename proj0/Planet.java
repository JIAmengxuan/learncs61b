public class Planet{
	
	double xxPos = 0; //Its current x position
	double yyPos = 0; //Its current y position
	double xxVel = 0; //Its current velocity in the x direction
	double yyVel = 0; //Its current velocity in the y direction
	double mass = 0; //Its mass
	String imgFileName = null; //The name of an image in the images directory that depicts the planet

	public Planet(double xP, double yP, double xV, double yV, double m, String img){
		xxPos = xP;
		yyPos = yP;
		xxVel = xV;
		yyVel = yV;
		mass = m;
		imgFileName = img;
	}

	public Planet(){
		xxPos = 0;
		yyPos = 0;
		xxVel = 0;
		yyVel = 0;
		mass = 0;
		imgFileName = null;
	}
	public Planet(Planet p){
		xxPos = p.xxPos;
		yyPos = p.yyPos;
		xxVel = p.xxVel;
		yyVel = p.yyVel;
		mass = p.mass;
		imgFileName = p.imgFileName;
	}

	/*return the distance between two Planets*/
	public double calcDistance(Planet p){
		double dy = p.yyPos - yyPos;
		double dx = p.xxPos - xxPos;
		double r = Math.sqrt(dx*dx + dy*dy);
		return r;
	}

	/*return the force that exterted on this planet by the given planet*/
	public double calcForceExertedBy(Planet p){
		double r = this.calcDistance(p);
		double g = 6.67*Math.pow(10,-11);
		double f = 	g * mass * p.mass /	(r*r);//force that p on this;
		return f;
	}

	/*return the x-component of the force that p on this*/
	public double calcForceExertedByX(Planet p){
		double f = this.calcForceExertedBy(p);
		double dx = p.xxPos - xxPos;//+(-)means that x-component of the force is positive(nega);
		double r = this.calcDistance(p);
		return (f * dx / r);
	}

	/*return the y-component of the force that p on this*/
	public double calcForceExertedByY(Planet p){
		double f = this.calcForceExertedBy(p);
		double dy = p.yyPos - yyPos;//+(-)means that x-component of the force is positive(nega);
		double r = this.calcDistance(p);
		return (f * dy / r);
	}

	public double calcNetForceExertedByX(Planet[] p){
		double fXnet = 0;
		for(int i = 0; i < p.length; i++){
			if(this.equals(p[i]) == false){
				fXnet += this.calcForceExertedByX(p[i]);	
			}			
		}
		return fXnet;
	}

	public double calcNetForceExertedByY(Planet[] p){
		double fYnet = 0;
		for(int i = 0; i < p.length; i++){
			if(this.equals(p[i]) == false){
				fYnet += this.calcForceExertedByY(p[i]);	
			}			
		}
		return fYnet;
	}

	public void update(double dt,double fX,double fY){
		double aX = fX / mass;
		xxVel = xxVel + aX * dt;
		double aY = fY / mass;
		yyVel = yyVel + aY * dt;
		xxPos = xxPos + dt * xxVel;
		yyPos = yyPos + dt * yyVel;
	}

	public void draw(){
		StdDraw.picture(xxPos, yyPos, "images/" + imgFileName);
	}
}
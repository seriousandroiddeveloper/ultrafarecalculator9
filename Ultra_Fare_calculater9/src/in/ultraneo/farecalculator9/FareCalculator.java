package in.ultraneo.farecalculator9;

public class FareCalculator {
	
	public int filter(float t)
	{
		int distance;
		String temp = String.valueOf(t);
		String temp2 = temp.substring(temp.indexOf("."));
		
		String temp3 = temp.substring(0,temp.indexOf("."));
		int temp4 = Integer.valueOf(temp3);
		
		float diff= Float.valueOf(temp2);
		
		System.out.println(diff);
		if(diff>0)
		{
			distance = temp4;
			distance = distance +1;
			
			
			
		}
		else 
		{
			distance = temp4;
			
		}
		
		
		return distance;
		
	}
	public float getFaretaxi(int distance,float start,float rest)
	{
		float distancetmp=0;
		
		distancetmp=start;
		
		distance= distance-1;
		
		if(distance>0)
		{
			distancetmp = distancetmp+distance*rest;
		}
		
		
		
		
		return distancetmp;
	}
	public float getFareauto(int distance,float start,float rest)
	{
		float distancetmp=0;
		
		distancetmp=start;
		
		distance= distance-2;
		
		if(distance>0)
		{
			distancetmp = distancetmp+distance*rest;
		}
		
		
		
		
		return distancetmp;
	}
	
	
	

}

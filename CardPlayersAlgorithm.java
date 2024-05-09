

import java.util.*;

class Pair
{
    protected int x;
    protected int y;

    public Pair(int x, int y)
    {
		this.x = x;
		this.y = y;
    }

    @Override
    public int hashCode()
    {
		int hash = 7;
		hash = 97 * hash + this.x;
		hash = 97 * hash + this.y;
		return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
		if (this == obj) return true;		
		if (obj == null) return false;		
		if (getClass() != obj.getClass()) return false;
		
		final Pair other = (Pair) obj;
		if (this.x != other.x) return false;		
		if (this.y != other.y) return false;
		
		return true;
    }    
}

public class CardPlayersAlgorithm
{
    private static int[] generate(int n, int min, int max)
    {
    	int a[] = new int[n];
    	for(int i=0; i<n; i++)
    	{
	    	a[i] = min + (int)((max-min+1) * Math.random());
		}
		return a;
    }
    
    private static boolean search(HashSet<Pair> sumList, int sx, int sy)
    {
		Pair sp = new Pair(sx, sy);
		return sumList.contains(sp);
    }

    private static int sign(int x, int y)
    {
		if(x<0 && y<0)
			return 1;
		else if(x>=0 && y>=0)
			return 1;
		else
			return 0;
    }
    
    private static HashSet<Pair> getRequirement(int p)
    {
		HashSet<Pair> set = new HashSet<Pair>();
		int abp = Math.abs(p);
		for(int y=-abp; y<=abp; y++)
		{
			if(sign(y,p)==1)
			{
				int d = Math.abs(Math.abs(y) - abp);	    
				set.add(new Pair(y,d));
			}
		}
		return set;
    }
    
    private static int searchInHand(ArrayList<Integer> hand, int elem)
    {
		int n = hand.size();
		for(int i=0; i<n; i++)
		{
			if(elem==hand.get(i)) return i;
		}
		return -1;
    }
    
    private static int getClosestElementIndex(ArrayList<Integer> hBob, HashSet<Pair> req)
    {
		if(req.size()==0) return -1;
		Iterator<Pair> it = req.iterator();
		Pair minPair = null;
		int yIndex = -1;
		
		while(it.hasNext())
		{
			Pair pair = it.next();	    
			int search = searchInHand(hBob,pair.x);
			
			if(search > -1)
			{
				if(minPair == null)
				{
					minPair = pair;
					yIndex = search;
				} else {
					if(pair.y <= minPair.y)
					{
						minPair = pair;
						yIndex = search;
					}
				}
			}
		}
		
		return yIndex;
    }
    
    private static int getMaxInHand(ArrayList<Integer> hand)
    {
		int n = hand.size();
		int s = hand.get(0);
		for(int i=1; i<n; i++)
		{
			if(s >= hand.get(i)) s = hand.get(i);
		}
		return s;
    }
    
    private static int getAppropriateDiscardIndex(ArrayList<Integer> hAlice, int sx)
    {
		int max = getMaxInHand(hAlice);
		HashSet<Pair> set = new HashSet<Pair>();
		int n = hAlice.size();
		
		Pair minPair = null;
		int minIndex = -1;
		
		for(int i=0; i<n; i++)
		{
			int e = hAlice.get(i);
			int s = sx - e;
			Pair p = new Pair(s,e);
			
			if(minPair == null) {
				minPair = p;
				minIndex = i;
			} else if(p.x <= minPair.x) {
				minPair = p;
				minIndex = i;
			}		
		}
		
		if(max!=hAlice.get(minIndex)) 
		{
			System.out.print("FALSE: ["+max+"," + sx+"]: ");
			for(int i=0; i<hAlice.size(); i++) System.out.print(hAlice.get(i)+",");
			System.out.println();
		}
		return minIndex;
    }
    
    private static ArrayList<Integer> getSubset(int a[], int n, int target)
    {
		int t = (n/2) + (n % 2);
		int sx = 0, sy = 0;
		
		HashSet<Pair> sumList = new HashSet<Pair>();
		ArrayList<Integer> x = new ArrayList<Integer>(t);
		ArrayList<Integer> y = new ArrayList<Integer>(t);
		
		for(int i=0; i<t; i++) 
		{
			sx += a[i];
			x.add(a[i]);
		}

		for(int i=t; i<n; i++) 
		{
			sy += a[i];
			y.add(a[i]);
		}

		while(sx != target)
		{
			int rc = 0;
			while(search(sumList, sx, sy))
			{
				rc++;
				int direction = (int)(100 * Math.random());
				if(direction >= 50)
				{
					int len = y.size();
					int choice = (int)(len * Math.random());
					int element = y.get(choice);
					y.remove(choice);
					x.add(element);
					sy -= element;
					sx += element;
				} else {
					int len = x.size();
					int choice = (int)(len * Math.random());
					int element = x.get(choice);
					x.remove(choice);
					y.add(element);
					sx -= element;
					sy += element;
				}

				if(rc==n) return null;				
			}

			HashSet<Pair> k = getRequirement(target - sx);
			int cei = getClosestElementIndex(y, k);
			if(cei >= 0)
			{
				int element = y.get(cei);
				y.remove(cei);
				x.add(element);
				sy -= element;
				sx += element;
			} else {
				if(x.size() > 1)
				{
					int di = getAppropriateDiscardIndex(x, sx);
					if(di == -1) return null;

					int element = x.get(di);
					y.add(element);
					x.remove(di);
					sy += element;
					sx -= element;
				} else {
					int len = y.size();
					int choice = (int)(len * Math.random());
					int element = y.get(choice);
					y.remove(choice);
					x.add(element);
					sy -= element;
					sx += element;
				}
			}

			sumList.add(new Pair(sx,sy));
		}
		return x;
    }
    
    public static void main(String args[])
    {
		// Test Code
		int n = 25;
		int a[] = generate(n,-100,300);
		int target = (int)(500*Math.random());
		for(int e:a) System.out.print(e+", ");
		System.out.println("\nTarget="+target);
		ArrayList<Integer> result = getSubset(a,n,target);
		if(result==null) System.out.println("Failure");
		int len = result.size();
		if(len==0) System.out.println("Failure");
		for(int i=0; i<len; i++) System.out.print(result.get(i)+", ");
		System.out.println();
    }
}
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;


public class StructureGenerator {

		// [n,0] innebär vilken section och [n,1] innebär vilket motiv
		// Lista med alla songer indelade i sections
		static List<ArrayList<Section>> songs = new ArrayList();
		static int[][] translationPositionProb = {{0,0},{0,1},{0,2},{0,3},
										   {1,0},{1,1},{1,2},{1,3},
										   {2,0},{2,1},{2,2},{2,3},
										   {3,0},{3,1},{3,2},{3,3}};
		
		static int[] translationPositionPorbSec = {1,2,3,4,12,13,14,23,24,34,123,124,134,234,1234};
		
		public StructureGenerator(String sections, String motives)
		{
			readSongStructures(sections, motives, "-",",",",");
		}
		
		public ArrayList<Motive> stealStructure() {
			ArrayList<Section> songStructure = songs.get((int)(songs.size()*Math.random()));
			ArrayList<Motive> newSongStructure = new ArrayList<Motive>();
			
			for(Section s : songStructure) {
				for(Motive m : s.getMotives()) {
					newSongStructure.add(m);
				}
			}
			
			return newSongStructure;
			
		}
		//////////////////////////////////////////////////////
		// Detta är metoden som skall anropas av andra klasser
		public ArrayList<Motive> generateNewStructure()
		{
		
			ArrayList<Section> sections = new ArrayList();
			
			ArrayList<Motive> result = new ArrayList();
			
			// Genererar motiv till första sektionen
			result.add(new Motive(1));
			double random = Math.random();
			System.out.println(trainNthSection(1).length);
			System.out.println();
			result.add(generateNthMotif(result, trainNthMotif(1), random));
			random = Math.random();
			result.add(generateNthMotif(result, trainNthMotif(2), random));
			random = Math.random();
			result.add(generateNthMotif(result, trainNthMotif(3), random));
			
			
			// Skapar sektioner där jag kan spara motiv
			ArrayList<Motive> section1 = new ArrayList();
			ArrayList<Motive> section2 = new ArrayList();
			ArrayList<Motive> section3 = new ArrayList();
			ArrayList<Motive> section4 = new ArrayList();
			
			section1.add(result.get(0));
			section1.add(result.get(1));
			section1.add(result.get(2));
			section1.add(result.get(3));
			sections.add(new Section(1, true, section1, 0));
			
			// Genererar sektioner
			int numbOfSection = generateNumbOfSections(6);
			for(int i = 1 ; i < numbOfSection ; i++)
			{
				random = Math.random();
				sections.add(generateSection(trainNthSection(i), random, sections));
			}
			
			// Genererar motiven
			int k = 0;
			for(Section sec : sections)
			{
				// Sannolikheter som skall tas bort
				int[] rem1 = new int[k];
				int[] rem2 = new int[k];
				int[] rem3 = new int[k];
				int[] rem4 = new int[k];
				
				for(int i = 0; i < k ; i++)
				{
					rem1[i]=1+4*i;
					rem2[i]=2+4*i;
					rem3[i]=3+4*i;
					rem4[i]=4+4*i;
				}
				
				int[] rem11 = new int[k*3];
				int[] rem22 = new int[k*3];
				int[] rem33 = new int[k*3];
				int[] rem44 = new int[k*3];
				
				for(int i = 0; i < k ; i++)
				{
					rem11[i*3]=2+4*i;
					rem11[i*3+1]=3+4*i;
					rem11[i*3+2]=4+4*i;
					
					rem22[i*3]=1+4*i;
					rem22[i*3+1]=3+4*i;
					rem22[i*3+2]=4+4*i;
					
					rem33[i*3]=1+4*i;
					rem33[i*3+1]=2+4*i;
					rem33[i*3+2]=4+4*i;
					
					rem44[i*3]=1+4*i;
					rem44[i*3+1]=2+4*i;
					rem44[i*3+2]=3+4*i;
				}
				
				// För att den skall hoppa över första sektionen
				if(k==0)
				{
					k++;
					continue;
				}
				
				// Om den är en ny sektion
				if(sec.isNew())
				{
					random = Math.random();
					result.add(generateNthMotif(result, removeUnwantMotProb(trainNthMotif(k*4),rem1), random));
					random = Math.random();
					result.add(generateNthMotif(result, removeUnwantMotProb(trainNthMotif(k*4+1),rem2), random));
					random = Math.random();
					result.add(generateNthMotif(result, removeUnwantMotProb(trainNthMotif(k*4+2),rem3), random));
					random = Math.random();
					result.add(generateNthMotif(result, removeUnwantMotProb(trainNthMotif(k*4+3),rem4), random));
				}
				// Om det inte är en ny sektion
				else
				{
					// Motiven som är identiska med ett annat motiv
					String identMot = Integer.toString(sec.getIdentMotives());
					
					// Sektionen och dens id som nya sektionen är identisk med
					int identSec = sec.getSectionID();
					Section tempSec = getSectionByID(sections, identSec);
					
					// Kollar alla fallen vilka motiv som skall vara identiska
					if(identMot.contains("1"))
					{
						random = Math.random();
						try{
							Motive mot = new Motive(tempSec.getMotives().get(0).getInex(),false,
									generateNthMotif(result, removeNewMotProb(removeUnwantMotProb(trainNthMotif(k*4),rem11)), 
											random).getSim());
							result.add(mot);
						}catch(Exception e)
						{
							System.out.print("Något blir fel");
							Motive mot = new Motive(tempSec.getMotives().get(0).getInex(),false,
									generateNthMotif(result, removeUnwantMotProb(trainNthMotif(k*4),rem11), 
											random).getSim());
							result.add(mot);
						}
						
					}
					else
					{
						random = Math.random();
						result.add(generateNthMotif(result, removeUnwantMotProb(trainNthMotif(k*4),rem1), random));
						
					}
					
					if(identMot.contains("2"))
					{
						random = Math.random();
						try{
							Motive mot = new Motive(tempSec.getMotives().get(0).getInex(),false,
									generateNthMotif(result, removeNewMotProb(removeUnwantMotProb(trainNthSection(k*4+1),rem22)), 
											random).getSim());
							result.add(mot);
						}catch(Exception e)
						{
							System.out.print("Något blir fel");
							Motive mot = new Motive(tempSec.getMotives().get(0).getInex(),false,
									generateNthMotif(result, removeUnwantMotProb(trainNthMotif(k*4+1),rem22), 
											random).getSim());
							result.add(mot);
						}
					
					}
					else
					{
						random = Math.random();
						result.add(generateNthMotif(result, removeUnwantMotProb(trainNthMotif(k*4+1),rem1), random));
						
					}
					
					if(identMot.contains("3"))
					{
						random = Math.random();
						try{
							Motive mot = new Motive(tempSec.getMotives().get(0).getInex(),false,
									generateNthMotif(result, removeNewMotProb(removeUnwantMotProb(trainNthMotif(k*4+2),rem33)), 
											random).getSim());
							result.add(mot);
						}catch(Exception e)
						{
							System.out.print("Något blir fel");
							Motive mot = new Motive(tempSec.getMotives().get(0).getInex(),false,
									generateNthMotif(result, removeUnwantMotProb(trainNthMotif(k*4+2),rem33), 
											random).getSim());
							result.add(mot);
						}
					
					}
					else
					{
						random = Math.random();
						result.add(generateNthMotif(result, removeUnwantMotProb(trainNthMotif(k*4+2),rem1), random));
						
					}
					
					if(identMot.contains("4"))
					{
						random = Math.random();
						try{
							Motive mot = new Motive(tempSec.getMotives().get(0).getInex(),false,
									generateNthMotif(result, removeNewMotProb(removeUnwantMotProb(trainNthSection(k*4+3),rem44)), 
											random).getSim());
							result.add(mot);
						}catch(Exception e)
						{
							System.out.print("Något blir fel");
							Motive mot = new Motive(tempSec.getMotives().get(0).getInex(),false,
									generateNthMotif(result, removeUnwantMotProb(trainNthMotif(k*4+3),rem44), 
											random).getSim());
							result.add(mot);
						}
					}
					else
					{
						random = Math.random();
						result.add(generateNthMotif(result, removeUnwantMotProb(trainNthMotif(k*4+3),rem1), random));
						
					}
				}
				k++;
				
			}
			
			//generateNthMotif(ArrayList<Motive> motives,double[] probs, double random/*, int newIndex*/)
			//removeUnwantMotProb(double[] probs,int[] pos)
			//trainNthMotif(int N)
			return result;
		}

		private static Section getSectionByID(ArrayList<Section> sections, int id)
		{
			int k = 0;
			for(Section sec : sections)
			{
				if(sec.getSectionID()==id)
				{
					return sec; 
				}
				k++;
			}
			return null;
		}
		
		// Färdig för test // Fungerar bra efter test
		////////////////////////////////////////////////////////////////////
		// Läser int filer och skapar strukturlistor
		// Läser in alla songers strukturer ifrån en textfil och fyller songs
		private void readSongStructures(String structureList, String motifList, String lineSeparator, String sepMot, String sepStruc)
		{

			try
			{
				// Läser in de två olika textfilerna en med motiv och en med struktur
				BufferedReader bf1 = new BufferedReader(new FileReader(motifList));
				BufferedReader bf2 = new BufferedReader(new FileReader(structureList));
				// Textsträngar som ändras för varje ny rad
				String motifLine, structureLine;
				// Denna skall tildelas värden från textfilerna
				ArrayList<Section> song = new ArrayList();
				// Ser till att songs är tom varje ny gång metoden anropas så att inte den fylls på
				// mer än en gång
				songs.clear();
				
				// Denna loopen läser in ???
				// loopen pågår tills textfilens rader tar slut
				while((motifLine=bf1.readLine())!=null&&(structureLine=bf2.readLine())!=null)
				{
					// När en radseparator kommer skall en song läggas till och en ny skapas
					if(motifLine.equals(lineSeparator))
					{
						songs.add(song);
						song = new ArrayList();
						continue;
					}
					
					StringTokenizer tz1 = new StringTokenizer(motifLine,sepMot);
					//StringTokenizer tz2 = new StringTokenizer(structureLine,sepStruc);
					
					// Alla instanser en Section behöver
					int secID = Integer.parseInt(structureLine.substring(0,1));
					Boolean isNew = true;/*= false*/;
					ArrayList<Motive> motives = new ArrayList();
					
					
					// Skapar motivlistan som ska läggas till Section
					// Jag gör alla bars till 0 för jag vet inte vad vi ska ha den instansen till
					while(tz1.hasMoreElements())
					{
						String mot = tz1.nextToken();
						int motIndex;
						if(mot.length()==2)
						{
							motIndex = Integer.parseInt(mot.substring(0,1));
						}
						else
						{
							motIndex = Integer.parseInt(mot.substring(0,2));	
							mot = mot.substring(1,mot.length());
						}
						Boolean motVar;
						Percentage perc = null;
						if(mot.substring(1,2).equals("0"))
						{
							motVar = true;
							motives.add(new Motive(motIndex));
						}
						else
						{
							motVar = false;
							if(mot.substring(1,2).equals("1"))
							{
								perc = Percentage.HUNDRED;
							}
							else if(mot.substring(1,2).equals("2"))
							{
								perc = Percentage.EIGHTY;
							}
							else if(mot.substring(1,2).equals("3"))
							{
								perc = Percentage.SIXTY;
							}
							motives.add(new Motive(motIndex,motVar,perc));
						}
					}
					int identMot = 0;
					String secStr = structureLine.substring(1,structureLine.length());
					if(secStr.substring(0,1).equals("N"))
					{
						isNew = true;
						identMot = 0;
					}
					else if(secStr.substring(0,1).equals("R"))
					{
						isNew = false;
						identMot = Integer.parseInt(secStr.substring(1,secStr.length()));
					}
					
					song.add(new Section(secID, isNew, motives, identMot));
				}
				// Kan behövas läggas till senare
				//songs.add(song);
				
				
				
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}	

		
		// Fungerar bra efter test
		private void printStructure(int song, int section, int motive)
		{
			// Kontroll av att section blir rätt för given section
			System.out.println(songs.get(song-1).get(section-1).getSectionID());
			System.out.println(songs.get(song-1).get(section-1).isNew());
			System.out.println(songs.get(song-1).get(section-1).getIdentMotives());
						
			// Kontroll av att motiv blir rätt för givet motiv
			System.out.println(songs.get(song-1).get(section-1).getMotives().get(motive-1).getInex());
			System.out.println(songs.get(song-1).get(section-1).getMotives().get(motive-1).isNew());
			System.out.println(songs.get(song-1).get(section-1).getMotives().get(motive-1).getSim());			
		}
		
		
		// Färdig för test // Fungerar bra efter test
		////////////////////////////////////////////////
		// Bestämmer antalet sektioner som skall skapas
		private int generateNumbOfSections(int maxNumbOfSection)
		{
			double [] probs = new double[maxNumbOfSection];
			
			for(ArrayList<Section> song : songs)
			{
				System.out.println();
				System.out.println(song.size());
				System.out.println();
				probs[song.size()]++;
			}
			
			int i = 0;
			for(double prob : probs)
			{
				probs[i] = prob/songs.size();
				i++;
			}
			
			int k = 0;
			double sum = 0;
			double epsi = 0.009;
			double random = Math.random();
			try
			{
				while(sum <= random - epsi)
				{
					sum += probs[k];
					k++;
				}
				return k-1;
			}
			catch(IndexOutOfBoundsException e)
			{
				e.printStackTrace();
				System.out.print("Något är fel för generering av antalet sektioner");
				return 2;
			}
		}
		
		// Färdig för test // Sannolikheterna summerar till 1 // N större än 8 ger inte exception
		/////////////////////////////////////////////////////////////////////////////////////////////////////
		// Jag vet inte om sannolikheterna blir rätt ?????? // Ska eventuellt kunna gå baklänges i looparna
		/////////////////////////////////////////////////////////////////////////////////////////////////////
		// N måste vara större än 0
		private double[] trainNthMotif(int N)
		{
			Motive temporaryMot, temporaryCompareMot;
			int[] result = new int[1+3*N];
			double[] resultProb = new double[1+3*N];
			
			int minusNumb = 0;
			for(int j = 0;j < songs.size() ;j++)
			{
				// Använd om fler än två sektioner
				if(songs.get(j).size()<=2 && N >= 8)
				{
					//System.out.print(songs.get(j).size());
					minusNumb++;
					continue;
				}
				
				if(songs.get(j).size()<=3 && N >= 12)
				{
					//System.out.print(songs.get(j).size());
					minusNumb++;
					continue;
				}
				
				temporaryMot = songs.get(j).get(translationPositionProb[N][0]).getMotives().get(translationPositionProb[N][1]);
				temporaryCompareMot = songs.get(j).get(translationPositionProb[0][0]).getMotives().get(translationPositionProb[0][1]);
				
				if(temporaryMot.isNew)
				{
					result[0] ++;
					continue;
				}
				
				if(/*temporaryMot.getSim().equals(temporaryCompareMot.getSim())&&*/temporaryMot.getInex() == temporaryCompareMot.getInex())
				{
					switch(temporaryMot.getSim())
					{
					case SIXTY:
						result[1] ++;
						break;
					case EIGHTY:
						result[2] ++;
						break;
					case HUNDRED:
						result[3] ++;
						break;
					}
					continue;
				}
				for(int i = 1; i < N ;i++)
				{
					
					temporaryCompareMot = songs.get(j).get(translationPositionProb[i][0]).getMotives().get(translationPositionProb[i][1]);
					
					if(temporaryMot.getInex() == temporaryCompareMot.getInex()/*temporaryMot.getSim().equals(temporaryCompareMot.getSim())*/)
					{
						int k = 3*i;
						switch(temporaryMot.getSim())
						{
						case SIXTY:
							result[k+1] ++;
							//System.out.println(result[k+1]);
							break;
						case EIGHTY:
							result[k+2] ++;
							//System.out.println(result[k+2]);
							break;
						case HUNDRED:
							result[k+3] ++;
							//System.out.println(result[k+3]);
							break;
						}
						break;
						
					}
				}
			}

			int n = 0;
			for(int i: result)
			{
				resultProb[n] = ((double)result[n])/(/*(double)*/songs.size()-minusNumb);
				n++;
			}
			
			return resultProb;
		}

		
		// Har testats med ett exempel N=3 och ta bort 1:an, Det fungerade
		////////////////////////////////////////////////////////////////////////////////////
		// Tar bort de sannolikheter som är oönskade pga att de redan skulle ha genererats
		private double[] removeUnwantMotProb(double[] probs,int[] pos)
		{
			double[] result = probs;
			for(int position : pos)
			{
				result[position*3] = 0;
				result[position*3-1] = 0;
				result[position*3-2] = 0;
			}
			
			double sum = 0;
			
			for(double prob: result)
			{
				sum = sum + prob;
			}
			int i = 0;
			for(double prob: result)
			{
				result[i] = prob/sum;
				i++;
			}
			
			return result;
		}

		// Sannolikheterna summerar ihop till 1 
		/////////////////////////////////////////////////
		// jag vet inte om det är rätt sannolikheter ty 
		// jag har inte den data jag behöver för att testa detta
		/////////////////////////////////////////////////////////////////////////////////////////
		// Tränar för genereing av Section N måste vara större än 0
		private double[] trainNthSection(int N)
		{
			int size = 1+N*15;
			double[] result = new double[size];
			for(ArrayList<Section> song : songs)
			{
				
				double minusNumb = 0;
				if(song.size()<=2&&N>1)
				{
					minusNumb ++;
					continue;
				}
				if(song.size()<=3&&N>2)
				{
					minusNumb ++;
					continue;
				}
				
				Section sec = song.get(N);
				if(sec.isNew())
				{
					result[0]++;
				}
				int i = 0;
				for(Section secCompare:song)
				{
					/*
					if(i == 0)
					{
						i++;
						continue;
					}
					*/
					i++;
					if(i+1 == N)
					{
						break;
					}
					if(sec.getSectionID()==secCompare.getSectionID())
					{
						// någonting mer
						if(sec.getIdentMotives()==1)
						{
							result[1*i]++;
						}
						else if(sec.getIdentMotives()==2)
						{
							result[2*i]++;	
						}
						else if(sec.getIdentMotives()==3)
						{
							result[3*i]++;
						}
						else if(sec.getIdentMotives()==4)
						{
							result[4*i]++;
						}
						else if(sec.getIdentMotives()==12)
						{
							result[5*i]++;
						}
						else if(sec.getIdentMotives()==13)
						{
							result[6*i]++;
						}
						else if(sec.getIdentMotives()==14)
						{
							result[7*i]++;
						}
						else if(sec.getIdentMotives()==23)
						{
							result[8*i]++;
						}
						else if(sec.getIdentMotives()==24)
						{
							result[9*i]++;
						}
						else if(sec.getIdentMotives()==34)
						{
							result[10*i]++;
						}
						else if(sec.getIdentMotives()==123)
						{
							result[11*i]++;
						}
						else if(sec.getIdentMotives()==124)
						{
							result[12*i]++;
						}
						else if(sec.getIdentMotives()==134)
						{
							result[13*i]++;
						}
						else if(sec.getIdentMotives()==234)
						{
							result[14*i]++;
						}
						else if(sec.getIdentMotives()==1234)
						{
							result[15*i]++;
						}
						break;
					}
				}
				
				}
			
			double sum = 0;
			for(double res: result)
			{
				sum+=res;
			}
			int j = 0;
			for(double res: result)
			{
				result[j]=res/sum;
				j++;
			}
			return result;
		}
		
		
		// Fungerar, ger inte längre några exceptions, men jag vet inte om den ger helt rätt
		////////////////////////////////////////////////////////////////////////////////////
		// Färdig för att testas
		
		
		private Motive generateNthMotif(ArrayList<Motive> motives,double[] probs, double random/*, int newIndex*/)
		{

			int incr = 0;
			
			
			double epsilon = 0.0001;
			double sum = 0;
			if(random<probs[0])
			{
				return new Motive(getHighestMotiveID(motives)+1);
			}
			sum = sum + probs[0];
			
			int i = 0;
			System.out.print(probs.length);
			for(double prob: probs)
			{
				if(i==0)
				{
					i++;
					continue;
				}
				sum += prob;
				if(random < sum-epsilon)
				{
					Percentage perc = null;
					if(i%3==0)
					{
						perc = Percentage.SIXTY;
					}
					else if(i%3==2)
					{
						perc = Percentage.EIGHTY;
					}
					else if(i%3==1)
					{
						perc = Percentage.HUNDRED;
					}
					return new Motive(motives.get((i-1)/3).getInex(), false, perc); 
				}
				System.out.println(incr++);
				i++;
			}
			return null;
		}
		
		// Denna borde fungera men har inte testats
		
		private int getHighestSectionID(ArrayList<Section> sec)
		{
			int result = sec.get(0).getSectionID();
			for(Section se: sec)
			{
				if(se.getSectionID()>result)
				{
					result = se.getSectionID();
				}
			}
			return result;
		}
		
		// Denna borde fungera men har inte testats
		private int getHighestMotiveID(ArrayList<Motive> motives)
		{
			int result = motives.get(0).getInex();
			for(Motive motive : motives)
			{
				// Tillfällig lösning vet inte varför problemet dyker upp
				if(motive==null)
				{
					continue;
				}
				if(motive.getInex()>result)
				{
					result = motive.getInex();
				}
			}
			return result;
		}
		
		///////////////////////////////////////////////////////////////////////////////////
		// Den har testats och ger inge exceptions men jag vet inte om den alltid gör rätt
		// Den innersta if-satsen behövs för att inte index out of bounds skall ske 
		// jag vet inte varför????
		private Section generateSection(double[] probs, double random, ArrayList<Section> sec)
		{
			double sum = 0;
			double epsilon = 0.00001;
			if(probs[0]>random)
			{
				return new Section(getHighestSectionID(sec)+1,true,null,0);
			}
			int j = 0;
			for(double prob: probs)
			{
				sum += prob;
				int k = 0;
				if(sum-epsilon > random)
				{
					if(j>=15){
						k++;
					}
					return new Section(sec.get(j/15-k).getSectionID(),false,null,translationPositionPorbSec[j%15]);
				}
				j++;
			}
			return null;
		}
		

		// Denna metod tar bort sannolikheten för att generera ett nytt motiv
		// Detta är nödvändigt om jag har genererat en sektion med lika motiv till
		// en annan sektion ty då skall inte ett nytt motiv genereras		
		private double[] removeNewMotProb(double[] probs)
		{
			double[] result = probs;
			
				result[0] = 0;
				
			
			double sum = 0;
			
			for(double prob: result)
			{
				sum = sum + prob;
			}
			int i = 0;
			for(double prob: result)
			{
				result[i] = prob/sum;
				i++;
			}
			
			return result;
		}
		
}

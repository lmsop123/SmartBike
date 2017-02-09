	
import javax.imageio.ImageIO;
import javax.swing.*;
import java.applet.Applet;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.File;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;




public class main_Application extends Applet implements KeyListener, Runnable {
 
	
	protected BufferedImage Left_Turn_Signal, Right_Turn_Signal, Contour, Contour11;
	protected static int xc10, yc10;
	protected static int xc11, yc11;
	protected static int xc12, yc12;
	protected static int Left_Turn_Signalx,Left_Turn_Signaly;
	protected static int Right_Turn_Signalx, Right_Turn_Signaly;
	protected static boolean right_signal_flash, left_signal_flash;
	protected static KeyEvent marshaller;
	protected static String Contour_State;
        protected static final String Project_Base_Directory = "/home/pi/java/RaspberryPie/raspberrypie";        

	
    private static class Contour_Displacement implements Runnable{
	
		
		
		public void run(){
	    	
			while(true){
	 		    	
                    
                    try{
                        Process p = Runtime.getRuntime().exec("sudo gpio -g read 4");
                        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
                        BufferedReader stdErr = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                        
                     //----------------------------------------------------------------------------------------------
                       Process p1 = Runtime.getRuntime().exec("sudo gpio -g read 14");
   		       BufferedReader stdInput1 = new BufferedReader(new InputStreamReader(p1.getInputStream()));
                       BufferedReader stdErr1 = new BufferedReader(new InputStreamReader(p1.getErrorStream())); 

                      if(stdErr.readLine() != null)
                          System.out.println("Error" + stdErr.readLine());
                      else if (stdErr1.readLine() != null)
                          System.err.println("Error" + stdErr1.readLine());
          
				
				
				if(stdInput.readLine().equals("1")){
					
					Right_Turn_Signalx = 1105 - 80;
					Right_Turn_Signaly = 80;
				        Process on = Runtime.getRuntime().exec("sudo gpio -g write 17 1");
					delay(320);
                                        Process off = Runtime.getRuntime().exec("sudo gpio -g write 17 0");
					Right_Turn_Signalx = -500;
					Right_Turn_Signaly = -500;
					//light_system.Led_right_seTo("low");
					delay(320);
                                }
				else if(stdInput1.readLine().equals("1")){
					
					Left_Turn_Signalx = 80;
					Left_Turn_Signaly = 80;
					Process on1 = Runtime.getRuntime().exec("sudo gpio -g write 15 1");
					delay(320);
                                        Process off1 = Runtime.getRuntime().exec("sudo gpio -g write 15 0");
					Left_Turn_Signalx = -500;
					Left_Turn_Signaly = -500;
				    //light_system.Led_left_seTo("low");
					delay(320);
				}

                          }catch(IOException e){
                             e.printStackTrace();}
			}		
			
		}
		
	    private void delay(int milliseconds){
	    	
	    	try {
				Thread.sleep(milliseconds);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    }
	}
	
	
	public void run(){
	    	
		BufferedImage tmp = Contour;//int top_contour_initial_offset = 1;
		BufferedImage tmp11 = Contour11;
       		String state = "left";
		    
        while(true){     


        if(marshaller != null){
			
		     if(marshaller.getKeyCode() == 38) {
		       		
				switch(state){
				
				case "left": 
					
					if(yc11 < 1){
						Contour11 = tmp;
						state = "top";
					}
					else
						yc11 = yc11 - 10;
				break;
				
				case "top":
					
					if(xc11 > 868){
						xc11 = xc11 + 266;
						Contour11 = tmp11;
						state = "right";
					}
					else
						xc11 = xc11 + 10;
			        break;    
				
				case "right": 
					
				   	if(yc11 > 398){
				   		yc11 = yc11 + 238;
				   		xc11 = xc11 - 262;
				   		Contour11 = tmp;
				   		state = "bottom";
				   	}
				   	else
				   		yc11 = yc11  + 10;
			        break;
			       
				case "bottom":
					
					if(xc11 < 0){
						yc11 = yc11 - 238;
						Contour11 = tmp11;
						state = "left";
					}
					else 
						xc11 = xc11 - 10;
					break;
			        
		        }
				
				
		 }
		   
	         else if(marshaller.getKeyCode() == 40){
				
			  switch(state){
			     
			  case "left":
				  if(yc11 >= 377){
					  Contour11 = tmp;
				      yc11 = yc11 + 256;
				      state = "bottom";
				  }
					  
				  else
					  yc11 = yc11 + 10;
			      break;
			      
			  case "bottom":  
				  if(xc11 >= 855){
					  Contour11 = tmp11;
				      yc11 = yc11 - 238;
				      xc11 = xc11 + 275;
				      state = "right";
				   }
				  else
					  xc11 = xc11 + 10;
				  break;
			      
			  case "right":
				  
				  if(yc11 <= 0 ){
					  Contour11 = tmp;
				      xc11 = xc11 - 200;
				      state = "top";
				  }
				  else
					  yc11 = yc11 - 10;
			      break;
			      
			  case "top":
				  
				  if(xc11 <= 0){
					  Contour11 = tmp11;
				      state = "left";
				  }
				  
				  else
					  xc11 = xc11 - 10;
			      break;
			  }
	         	 
			}
		

}
		
		
			    try {
				Thread.sleep(10);
		   	} catch (InterruptedException e) {
				// TODO Auto-generated catch block

			e.printStackTrace();}
                         repaint();			
			
              }

			
		}
	
		

	public void keyPressed(KeyEvent e){
		
		    
        if (e.getKeyCode() == 38){
        	Contour_State = "Clockwise";
        	marshaller = e;
        	
        }
		
        else if (e.getKeyCode() == 40){
        	Contour_State = "CounterClockwise";
        	marshaller = e;
        }
	
        
        else if(e.getKeyCode() == 37){
		    left_signal_flash = true;
		}
		
		else if (e.getKeyCode() == 39)	{
			right_signal_flash = true;
		
			
		}
	}
	
	
	
	public void keyReleased(KeyEvent e){
		
		   if (e.getKeyCode() == 37){
			   left_signal_flash = false;
			   Left_Turn_Signalx = -500;
			   Left_Turn_Signaly = -500;
		   }
		   
		   
		   else if(e.getKeyCode() == 38){
			   Contour_State = "Neutral";
		
	   marshaller = null;
		   }
		   
		   else if(e.getKeyCode() == 40){
			   marshaller = null;
			   Contour_State = "Neutral";
		   }
			   
		   
		   else if(e.getKeyCode() == 39){
			   right_signal_flash = false;
			   Right_Turn_Signalx = -500;
			   Right_Turn_Signaly = -500;
			   

		   }
		
	}
	
	
	public void keyTyped(KeyEvent e){}	
	

    public void init(){
		
		try{
 
        Process pre_setup = Runtime.getRuntime().exec("sudo gpio -g mode 4 input");
        Process pre_setup_1 = Runtime.getRuntime().exec("sudo gpio -g mode 17 output");
        Process pre_setup_2 = Runtime.getRuntime().exec("sudo gpio -g write 17 0"); 
        Process pre_setup_3 = Runtime.getRuntime().exec("sudo gpio -g mode 15 output");
        Process pre_Setup_4 = Runtime.getRuntime().exec("sudo gpio -g write 0 15");
        Process pre_setup_5 = Runtime.getRuntime().exec("sudo gpio -g mode 14 input");
 
        Process pre_setup_6 = Runtime.getRuntime().exec("/bin/bash /home/pi/java/Sonar/DetectProxim");
        
		}catch (IOException e){
			e.printStackTrace();
		}
    	
    	
    	int out_of_bounds = -500;
    	right_signal_flash = false;
    	left_signal_flash = false;
    	Contour_State = "Neutral";
    	Right_Turn_Signalx = out_of_bounds;
    	Right_Turn_Signaly = out_of_bounds;
    	Left_Turn_Signalx = out_of_bounds;
    	Left_Turn_Signalx = out_of_bounds;
    	xc11 = 0;
    	yc11 = 200;
    	
    	//initialise the top horizontal block at rest position
    	xc10 = -350;
    	yc10 = 0;
    	
    	//intialise the bottom horizontal block at the rest position.  

    	xc12 = -350;
    	yc12 = 638;
    	 	
    	
	    setSize(1200,700);
		setBackground(Color.BLUE);
		
		
		try{
			URL imagesrc = (new File( Project_Base_Directory + "/images/left_turn_signal_on.jpg")).toURI().toURL();
			URL imagesrc1 = (new File( Project_Base_Directory + "/images/Right_turn_signal_on.png")).toURI().toURL();
			URL imagesrc2 = (new File( Project_Base_Directory +  "/images/Contour10.png")).toURI().toURL();

			URL imagesrc3 = (new File( Project_Base_Directory + "/images/Contour11.png")).toURI().toURL();
			Left_Turn_Signal = ImageIO.read(imagesrc);
			Right_Turn_Signal = ImageIO.read(imagesrc1);
			Contour = ImageIO.read(imagesrc2);
			Contour11 = ImageIO.read(imagesrc3);
	
			
			

		}catch(MalformedURLException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
	    }
   
	
	   Thread t1 = new Thread(new Contour_Displacement());
	   Thread t2 = new Thread(this);
	   
	    
	    t1.start();
	    t2.start();  
	    
	    addKeyListener(this);
	
	}
    
    
//min for y contour11 -322
//max for y contour11 380
 
 @Override 
 public void paint(Graphics g){
		
                          
                BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
	        
              try{
                animation(img.getGraphics());
                g.drawImage(img, 0 , 0, null);
		}catch(Exception e){
			e.printStackTrace(); }
               // g.drawImage(Right_Turn_Signal, Right_Turn_Signalx, Right_Turn_Signaly, this);
               // g.drawImage(Left_Turn_Signal, Left_Turn_Signalx , Left_Turn_Signaly, this);
               // g.drawImage(Contour, xc10, yc10, this);
               // g.drawImage(Contour11, xc11, yc11, this);
               // g.drawImage(Contour, xc12, yc12, this);
 		

	}
 
@Override 
public void update(Graphics g){
         
               paint(g);

    }

  public void animation(Graphics g){
            
            //super.paint(g);
            g.drawImage(Right_Turn_Signal, Right_Turn_Signalx, Right_Turn_Signaly, this);
            g.drawImage(Left_Turn_Signal, Left_Turn_Signalx, Left_Turn_Signaly, this); 
            g.drawImage(Contour, xc10,yc10,this);
	    g.drawImage(Contour11, xc11, yc11, this);
   	     

}

}

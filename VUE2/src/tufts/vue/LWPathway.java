/*
 * LWPathway.java
 *
 * Created on June 18, 2003, 1:37 PM
 */

package tufts.vue;

import java.util.LinkedList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.BasicStroke;
import java.awt.geom.Area;
import java.util.ArrayList;
/**
 *
 * @author  Jay Briedis
 */
public class LWPathway extends tufts.vue.LWComponent implements Pathway
{
    private LinkedList elementList = null;
    private int weight = 1;
    private String comment = "";
    private boolean ordered = false;
    private Color borderColor = Color.blue;
    private LWMap map = null;
    
    private int currentIndex;
    
    /**default constructor used for marshalling*/
    public LWPathway() {
        //added by Daisuke
        elementList = new LinkedList();    
        currentIndex = -1;
    }
    
    public LWPathway(LWMap map, String label) {
        this(label);
        this.map = map;        
    }
    
    /** Creates a new instance of LWPathway with the specified label */
    public LWPathway(String label) {
        super.setLabel(label);
        elementList = new LinkedList();
        currentIndex = -1;
    }
     
    /** adds an element to the end of the pathway */
    public void addElement(LWComponent element) {
       elementList.add(element); 
       if (currentIndex == -1) currentIndex = length() - 1;
       //maybe need to repaint the view?
    }
    
    public LWMap getPathwayMap(){
        return map;
    }
    
    public void drawPathway(Graphics2D g){
        Iterator iter = this.getElementIterator();
        Color oldColor = g. getColor();
        while(iter.hasNext()){
            LWComponent comp = (LWComponent)iter.next();
            g.setColor(borderColor);
            if(comp instanceof LWNode)
                g.draw(comp.getShape());      
            else if(comp instanceof LWLink){
                LWLink link = (LWLink)comp;
                LWComponent ep1 = link.getComponent1();
                LWComponent ep2 = link.getComponent2();
                if (!(ep1 instanceof LWLink && ep2 instanceof LWLink)
                && !(ep1.getShape() == null && ep2.getShape() == null)) {
                Area clipArea = new Area(g.getClipBounds());
                if (!(ep1 instanceof LWLink) && ep1.getShape() != null)
                    clipArea.subtract(new Area(ep1.getShape()));
                if (!(ep2 instanceof LWLink) && ep2.getShape() != null)
                    clipArea.subtract(new Area(ep2.getShape()));
                g.clip(clipArea);
            }
                g.draw(link.getLine());
            }
        }
        g.setColor(oldColor);
    }
    
    public boolean contains(LWComponent element){
        return elementList.contains(element);
    }
    
    public int length() {
        return elementList.size();
    }
    
    public LWComponent getFirst() {        
        LWComponent firstElement = null;
        try{
            firstElement = (LWComponent)elementList.getFirst();
            currentIndex = 0;
        }catch(NoSuchElementException ne){
            firstElement = null;
        }        
        return firstElement;
    }
    
    public boolean isFirst(){

        return (currentIndex == 0);
    }
    
    public LWComponent getLast() {        
        LWComponent lastElement = null;        
        try{
            lastElement = (LWComponent)elementList.getLast();
            currentIndex = length() - 1;
        }catch(NoSuchElementException ne){
            lastElement = null;
        }
        
        return lastElement;
    }
    
    public boolean isLast(){
        return (currentIndex == (length() - 1));
    }
      
    public LWComponent getPrevious(){
        if (currentIndex > 0)
            return (LWComponent)elementList.get(--currentIndex);        
        else
            return null;
    }
    
    public LWComponent getNext(){
        if (currentIndex < (length() - 1))
            return (LWComponent)elementList.get(++currentIndex);        
        else 
            return null;
    }
    
    public LWComponent getElement(int index){
        LWComponent element = null;
        try{
            element = (LWComponent)elementList.get(index);
        }catch (IndexOutOfBoundsException ie){
            element = null;
        }       
        return element;
    }
    
    public java.util.Iterator getElementIterator() {
        return elementList.iterator();
    }
    
    public void removeElement(int index) {       
        if (index == currentIndex)
        {
            //gotta fix this
            if (!isFirst()){
              getPrevious();
              System.out.println("moved back to " + currentIndex);
            }else{
              System.out.println("moved forward to " + currentIndex);
            }
        }
        
        LWComponent element = (LWComponent)elementList.remove(index);
        
        if(element == null)
            System.err.println("LWPathway.removeElement: element does not exist in pathway");
    }
   
    public void moveElement(int oldIndex, int newIndex) {
        LWComponent element = getElement(oldIndex);
        removeElement(oldIndex);
        addElement(element, newIndex);
    }
    
    /**accessor methods used also by xml marshalling process*/
    public Color getBorderColor(){
        return borderColor;
    }
    
    public void setBorderColor(Color color){
        this.borderColor = color;
    }
    
    public int getWeight() {
        return weight;
    }
    
    public void setWeight(int weight) {
        this.weight = weight;
    }
    
    public boolean getOrdered() {
        return ordered;
    }
    
    public void setOrdered(boolean ordered) {
        this.ordered = ordered;
    }
    
    public java.util.List getElementList() {
        return elementList;
    }
    
    public void setElementList(java.util.List elementList) {
        this.elementList = (LinkedList)elementList;
        if (elementList.size() >= 1) currentIndex = 0;
    }
    
    /** Interface for the linked list used by the Castor mapping file*/
    public ArrayList getElementArrayList()
    {
        return new ArrayList(elementList);
    }
    
    public void setElementArrayList(ArrayList list)
    {
        elementList = new LinkedList(list);
    }
    /** end of Castor Interface */
    
    public LWComponent getCurrent() { 
        LWComponent element = null;        
        try{
            element = (LWComponent)elementList.get(currentIndex);
        }catch (IndexOutOfBoundsException ie){
            element = null;
        }      
        return element;
    }
    
    public String getComment(){
        return comment;
    }
    
    public void setComment(String comment){
        this.comment = comment;
    }
    
    public void setCurrentIndex(int i){
        currentIndex = i;
    }
    
    public int getCurrentIndex(){
        return currentIndex;
    }
   
/*****************************/    
/**methods below are not used*/    
/*****************************/
    
    /** adds an element at the specified location within the pathway*/
    public void addElement(LWComponent element, int index){
        if(elementList.size() >= index){
            elementList.add(index, element);
            //if(current == null) setCurrent(element);
            if (currentIndex == -1) currentIndex = index;
        }else{
            System.out.println("LWPathway.addElement(element, index), index out of bounds");
        }
    }
    
    /** adds an element in between two other elements, if they are adjacent*/
    public void addElement(LWComponent element, LWComponent adj1, LWComponent adj2){
        int index1 = elementList.indexOf(adj1);
        int index2 = elementList.indexOf(adj2);
        int dif = index1 - index2;
        if(elementList.size() >= index1 && elementList.size() >= index2){
            if(Math.abs(dif) == 1){
                if(dif == -1)
                    elementList.add(index2, element);
                else
                    elementList.add(index1, element);
            }
        }else{
            System.out.println("LWPathway.addElement(element,adj1,adj2), index out of bounds");
        }
    }
    
        
    
    public void removeElement(LWComponent element) {
       /* 
        if (element.equals(getCurrent()))
        {
            if (isFirst(element))
                setCurrent(getNext(element));
            
            else
                setCurrent(getPrevious(element));
        }
        
        boolean success = elementList.remove(element);
        if(!success)
            System.err.println("LWPathway.removeElement: element does not exist in pathway");
   */ }
    
    
    
    public LWComponent getNext(LWComponent current) {
        int index = elementList.indexOf(current);
        
        if (index >= 0 && index < (length() - 1))
          return (LWComponent)elementList.get(++index);
        
        //if (currentIndex >= 0 && currentIndex < (length() - 1))
          //return (LWComponent)elementList.get(currentIndex + 1);
        else
          return null;
    }
   
    
    
    public LWComponent getPrevious(LWComponent current) {
        int index = elementList.indexOf(current);
        
        if (index > 0)
          return (LWComponent)elementList.get(--index);
        
        //if (currentIndex > 0)
          //return (LWComponent)elementList.get(currentIndex - 1);
        else
          return null;
        
    }
    
}

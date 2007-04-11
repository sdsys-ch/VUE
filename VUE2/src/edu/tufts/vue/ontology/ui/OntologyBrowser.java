
/*
 *
 * * <p><b>License and Copyright: </b>The contents of this file are subject to the
 * Mozilla Public License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License
 * at <a href="http://www.mozilla.org/MPL">http://www.mozilla.org/MPL/.</a></p>
 *
 * <p>Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.</p>
 *
 * <p>The entire file consists of original code.  Copyright &copy; 2003-2007
 * Tufts University. All rights reserved.</p>
 *
 *
 */

package edu.tufts.vue.ontology.ui;

import java.awt.*;
import javax.swing.*;
import tufts.vue.*;
import tufts.vue.gui.*;

/*
 * OntologyBrowser.java
 *
 * Created on April 5, 2007, 2:15 PM
 *
 * @author dhelle01
 */
public class OntologyBrowser extends JPanel {
    
    public static final Object POPULATE_TYPES = "type_list_layout_constraint";
    
    final JPanel ontologiesPanel;
    //final Widget typesPane = null;
    final Widget typesPane = new Widget("types");
    
    final DockWindow dockWindow;
    final DockWindow ontologyDock;
    final DockWindow typeDock;
    
    //$
      private OntologyViewer ontologyViewer;
    //$
    
    // corresponds to searchPane from DRBrowser
    final JComponent populatePane = new Widget("Populate Types") {
            private Component editor, result;
            {
                setOpaque(false);
            }
            
            protected void addImpl(Component c, Object constraints, int idx) {
                JComponent jc = null;
                if (c instanceof JComponent)
                    jc = (JComponent) c;
                if (constraints == POPULATE_TYPES) {

                    // was SingleDockImpl in DRBrowser
                    if (true) {
                        // this method of setting this is a crazy hack for now, but
                        // it's perfect for allowing us to try different layouts
                        //typesPane.removeAll();
                        typesPane.add(jc);
                        //typesPane.setHidden(false);
                        typesPane.validate();
                        return;
                    }
                    
                    if (result != null)
                        remove(result);
                    result = c;
                    constraints = BorderLayout.CENTER;
                } else {
                    tufts.Util.printStackTrace("illegal type population constraints " + constraints);
                }
                
                super.addImpl(c, constraints, idx);
                revalidate();
            }


    };
    
    
    public OntologyBrowser(boolean delayedLoading, DockWindow ontologyDock,DockWindow typeDock) 
    {
        //super(new BorderLayout());
        //super(new java.awt.GridLayout(0,1));
        setName("Ontologies");
        
        this.dockWindow = ontologyDock;
        this.ontologyDock = ontologyDock;
        this.typeDock = typeDock;
        this.ontologiesPanel = this;
        
        if(delayedLoading)
        {
            //TBD see DRBrowser for likely path to take..
        }
        else
        {
            loadOntologyViewer();
        }
        
        buildSingleDockWindow();
        
        //$
          javax.swing.JButton button = new javax.swing.JButton("update");
          button.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
               java.util.List<edu.tufts.vue.ontology.Ontology> list = edu.tufts.vue.ontology.OntManager.getOntManager().getOntList();
               /*for(int i=0;i<list.size();i++)
               {
                   
               }*/
               System.out.println("Ontology Browser: OntManager list size: " + list.size());
               System.out.println("Ontology Browser: ontology viewer model size: " + ontologyViewer.getList().getModel().getSize());
               ontologyViewer.updateUI();
               ontologyViewer.getList().updateUI();
               revalidate();
               repaint();
               ontologyViewer.repaint();
               updateUI();
            }
          });
          add(button);
          
          Action[] actions = {
            new edu.tufts.vue.ontology.action.OntologyOpenAction("Add an Ontology"),
            new edu.tufts.vue.ontology.action.RDFSOntologyOpenAction("RDFS"),
            new edu.tufts.vue.ontology.action.OwlOntologyOpenAction("OWL")
          };
          tufts.vue.gui.Widget.setMenuActions(this,actions);
        //$
          
    }
    
    public void loadOntologyViewer()
    {
        //OntologyViewer ontologyViewer = new OntologyViewer(this);
        ontologyViewer = new OntologyViewer(this);
        ontologyViewer.setName("Ontology Viewer");
        ontologiesPanel.add(ontologyViewer);
        revalidate();
    }
    
    public void buildSingleDockWindow()
    {
        // may not need these next two lines, these are adapted from DRBrowser
        //typesPane.setTitleHidden(true);
        //typesPane.setHidden(true);
        
        WidgetStack stack = new WidgetStack(getName());

        Widget.setWantsScroller(stack, true);

        stack.addPane(ontologiesPanel, 0f);
        stack.addPane(populatePane,0f);
        stack.addPane(typesPane, 0f);
        
        this.dockWindow.setContent(stack);
    }
    
}

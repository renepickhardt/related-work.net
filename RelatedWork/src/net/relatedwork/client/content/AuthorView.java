package net.relatedwork.client.content;

import net.relatedwork.client.MainPresenter;
import net.relatedwork.client.staticpresenter.AboutPresenter;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class AuthorView extends ViewImpl implements AuthorPresenter.MyView {
	
	  public static interface CwConstants extends Constants {
		    String cwTabPanelDescription();

		    String cwTabPanelName();

		    String[] cwTabPanelTabs();
		  }

		  /**
		   * An instance of the constants.
		   */
		  private final CwConstants constants;
	
	
	private final Widget widget;
	
	private final HTMLPanel similarAuthors;
	private final HTMLPanel coAuthors;
	private final HTMLPanel citedAuthors;
	private final HTMLPanel citedByAuthors;	

	@UiField HTMLPanel paperByAuthor;
	@UiField HeadingElement authorName;
	@UiField DecoratedTabPanel authorListPanel;

	@UiField HTMLPanel rwDiscussions;

	
	public interface Binder extends UiBinder<Widget, AuthorView> {
	}

	@Inject
	public AuthorView(final Binder binder) {		
	    widget = binder.createAndBindUi(this);

	    similarAuthors = new HTMLPanel("");
	    coAuthors = new HTMLPanel("");
	    citedAuthors = new HTMLPanel("");
	    citedByAuthors = new HTMLPanel("");
	    
	    
	    
		constants = new CwConstants(){

			@Override
			public String cwTabPanelDescription() {
				return "Panel description";
			}

			@Override
			public String cwTabPanelName() {
				// TODO Auto-generated method stub
				return "Panel Tab Name";
			}
			@Override
			public String[] cwTabPanelTabs() {
				String[] array ={"Similar Authors (25)","Co-Authors (10)","Cited Authors (100)", "Cited by Authors (5)"}; 
				// TODO Auto-generated method stub
				return array;
			}};
		
		//TODO: fix static height http://stackoverflow.com/questions/5170324/tablayoutpanel-dynamic-resizing
	//	authorListPanel.setHeight("800px");
			
		authorListPanel.setWidth("400px");
		authorListPanel.setAnimationEnabled(true);

	//    authorListPanel.setAnimationDuration(1000);
	    authorListPanel.getElement().getStyle().setMarginBottom(10.0, Unit.PX);

	    // Add a home tab
	    String[] tabTitles = constants.cwTabPanelTabs();
	    authorListPanel.add(similarAuthors, tabTitles[0]);
	    authorListPanel.add(coAuthors, tabTitles[1]);
	    authorListPanel.add(citedByAuthors, tabTitles[2]);
	    authorListPanel.add(citedAuthors, tabTitles[3]);
	    
	    // Return the content
	    authorListPanel.selectTab(0);
	    //TODO: what is ensureDebugId?
	    authorListPanel.ensureDebugId("cwTabPanel");

	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	
	
	public void setInSlot(Object slot, Widget content) {
		if (slot == AuthorPresenter.TYPE_SimilarAuthors){
			setSimilarAuthors(content);			
		}
		else if (slot == AuthorPresenter.TYPE_Discussion) {
			setDiscussions(content);
		} 
		else if (slot == AuthorPresenter.TYPE_CoAuthors) {
			setCoAuthors(content);			
		} 
		else if (slot == AuthorPresenter.TYPE_CitedAuthors) {
			setCitedAuthors(content);			
		} 
		else if (slot == AuthorPresenter.TYPE_CitedByAuthors) {
			setCitedByAuthors(content);			
		} 
		else if (slot == AuthorPresenter.TYPE_Papers) {
			setPapers(content);			
		} 
		else {
			super.setInSlot(slot, content);
		}
	}
	
	public void setAuthorName(String name){
		authorName.setInnerHTML(name);
	}
	
	private void setPapers(Widget content) {
		paperByAuthor.clear();
		if(content!=null){
			paperByAuthor.add(content);
		}
	}

	private void setCitedByAuthors(Widget content) {
		citedAuthors.clear();
		if (content!=null){
			citedAuthors.add(content);
		}
	}

	private void setCitedAuthors(Widget content) {
		citedByAuthors.clear();
		if (content!=null){
			citedByAuthors.add(content);
		}
		
	}

	private void setCoAuthors(Widget content) {
		coAuthors.clear();
		if (content!=null){
			coAuthors.add(content);
		}
		
	}

	private void setSimilarAuthors(Widget content) {
		similarAuthors.clear();
		if (content != null) {
			similarAuthors.add(content);
		}		
	}

	
	public HTMLPanel getSimilarAuthors() {
		return similarAuthors;
	}

	public HTMLPanel getCoAuthors() {
		return coAuthors;
	}

	public HTMLPanel getCitedAuthors() {
		return citedAuthors;
	}

	public HTMLPanel getCitedByAuthors() {
		return citedByAuthors;
	}
	public HTMLPanel getPaperByAuthor() {
		return paperByAuthor;
	}

	public void setPaperByAuthor(HTMLPanel paperByAuthor) {
		this.paperByAuthor = paperByAuthor;
	}
		
	private void setDiscussions(Widget content) {
		rwDiscussions.clear();
		if (content != null) {
			rwDiscussions.add(content);
		}		
	}
}

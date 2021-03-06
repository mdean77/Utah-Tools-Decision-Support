package edu.utah.cdmcc.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

public class ShowRuleTraceHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		try{
			HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().showView("edu.utah.cdmcc.decisionsupport.drools.AuditView");
		} catch (PartInitException e){
			System.out.println("Part init exception opening audit view");
	}
		return null;
	}

}

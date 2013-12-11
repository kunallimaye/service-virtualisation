/**
 * 
 */
package com.kunal.demo.service.connect.util;

import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author kunallimaye
 *
 */
public class Resources {
	   @SuppressWarnings("unused")
	   @Produces
	   @PersistenceContext
	   private EntityManager em;
	   
	   @Named
	   @Produces
	   public Logger produceLogForFinanceStock(InjectionPoint injectionPoint) {
	      return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
	   }
	   
	   @Produces
	   @RequestScoped
	   public FacesContext produceFacesContext() {
	      return FacesContext.getCurrentInstance();
	   }

}

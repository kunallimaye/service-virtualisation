package com.kunal.demo.service.connect.view;

import com.kunal.demo.service.connect.model.ServiceEndpoint;
import com.kunal.demo.service.connect.view.ServiceEndpointBean;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.*;

@RunWith(Arquillian.class)
public class ServiceEndpointBeanTest
{
   @Inject
   private ServiceEndpointBean serviceendpointbean;

   @Deployment
   public static JavaArchive createDeployment()
   {
      return ShrinkWrap.create(JavaArchive.class, "test.jar")
    		.addClass(ServiceEndpoint.class)
            .addClass(ServiceEndpointBean.class)
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
   }

   @Test
   public void testIsDeployed()
   {
      Assert.assertNotNull(serviceendpointbean);
   }
}

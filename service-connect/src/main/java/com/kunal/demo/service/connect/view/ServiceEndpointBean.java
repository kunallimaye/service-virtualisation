package com.kunal.demo.service.connect.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.kunal.demo.service.connect.model.ServiceEndpoint;

/**
 * Backing bean for ServiceEndpoint entities.
 * <p>
 * This class provides CRUD functionality for all ServiceEndpoint entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class ServiceEndpointBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving ServiceEndpoint entities
    */

   private Long id;

   public Long getId()
   {
      return this.id;
   }

   public void setId(Long id)
   {
      this.id = id;
   }

   private ServiceEndpoint serviceEndpoint;

   public ServiceEndpoint getServiceEndpoint()
   {
      return this.serviceEndpoint;
   }

   @Inject
   private Conversation conversation;

//   @PersistenceContext(type = PersistenceContextType.EXTENDED)
   @Inject
   private EntityManager entityManager;

   public String create()
   {

      this.conversation.begin();
      return "create?faces-redirect=true";
   }

   public void retrieve()
   {

      if (FacesContext.getCurrentInstance().isPostback())
      {
         return;
      }

      if (this.conversation.isTransient())
      {
         this.conversation.begin();
      }

      if (this.id == null)
      {
         this.serviceEndpoint = this.example;
      }
      else
      {
         this.serviceEndpoint = findById(getId());
      }
   }

   public ServiceEndpoint findById(Long id)
   {

      return this.entityManager.find(ServiceEndpoint.class, id);
   }

   /*
    * Support updating and deleting ServiceEndpoint entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.serviceEndpoint);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.serviceEndpoint);
            return "view?faces-redirect=true&id=" + this.serviceEndpoint.getId();
         }
      }
      catch (Exception e)
      {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
         return null;
      }
   }

   public String delete()
   {
      this.conversation.end();

      try
      {
         ServiceEndpoint deletableEntity = findById(getId());

         this.entityManager.remove(deletableEntity);
         this.entityManager.flush();
         return "search?faces-redirect=true";
      }
      catch (Exception e)
      {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
         return null;
      }
   }

   /*
    * Support searching ServiceEndpoint entities with pagination
    */

   private int page;
   private long count;
   private List<ServiceEndpoint> pageItems;

   private ServiceEndpoint example = new ServiceEndpoint();

   public int getPage()
   {
      return this.page;
   }

   public void setPage(int page)
   {
      this.page = page;
   }

   public int getPageSize()
   {
      return 10;
   }

   public ServiceEndpoint getExample()
   {
      return this.example;
   }

   public void setExample(ServiceEndpoint example)
   {
      this.example = example;
   }

   public void search()
   {
      this.page = 0;
   }

   public void paginate()
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

      // Populate this.count

      CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
      Root<ServiceEndpoint> root = countCriteria.from(ServiceEndpoint.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<ServiceEndpoint> criteria = builder.createQuery(ServiceEndpoint.class);
      root = criteria.from(ServiceEndpoint.class);
      TypedQuery<ServiceEndpoint> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<ServiceEndpoint> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      String name = this.example.getName();
      if (name != null && !"".equals(name))
      {
         predicatesList.add(builder.like(root.<String> get("name"), '%' + name + '%'));
      }
      String description = this.example.getDescription();
      if (description != null && !"".equals(description))
      {
         predicatesList.add(builder.like(root.<String> get("description"), '%' + description + '%'));
      }
      String url = this.example.getUrl();
      if (url != null && !"".equals(url))
      {
         predicatesList.add(builder.like(root.<String> get("url"), '%' + url + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<ServiceEndpoint> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back ServiceEndpoint entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<ServiceEndpoint> getAll()
   {

      CriteriaQuery<ServiceEndpoint> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(ServiceEndpoint.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(ServiceEndpoint.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final ServiceEndpointBean ejbProxy = this.sessionContext.getBusinessObject(ServiceEndpointBean.class);

      return new Converter()
      {

         @Override
         public Object getAsObject(FacesContext context,
               UIComponent component, String value)
         {

            return ejbProxy.findById(Long.valueOf(value));
         }

         @Override
         public String getAsString(FacesContext context,
               UIComponent component, Object value)
         {

            if (value == null)
            {
               return "";
            }

            return String.valueOf(((ServiceEndpoint) value).getId());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private ServiceEndpoint add = new ServiceEndpoint();

   public ServiceEndpoint getAdd()
   {
      return this.add;
   }

   public ServiceEndpoint getAdded()
   {
      ServiceEndpoint added = this.add;
      this.add = new ServiceEndpoint();
      return added;
   }
}
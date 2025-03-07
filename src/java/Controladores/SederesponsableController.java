package Controladores;

import Modelos.Sederesponsable;
import Controladores.util.JsfUtil;
import Controladores.util.PaginationHelper;
import Repositorio.SederesponsableFacade;

import java.io.Serializable;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@Named("sederesponsableController")
@SessionScoped
public class SederesponsableController implements Serializable {

    private Sederesponsable current;
    private DataModel items = null;
    @EJB
    private Repositorio.SederesponsableFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public SederesponsableController() {
    }

    public Sederesponsable getSelected() {
        if (current == null) {
            current = new Sederesponsable();
            current.setSederesponsablePK(new Modelos.SederesponsablePK());
            selectedItemIndex = -1;
        }
        return current;
    }

    private SederesponsableFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Sederesponsable) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Sederesponsable();
        current.setSederesponsablePK(new Modelos.SederesponsablePK());
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            current.getSederesponsablePK().setCodSede(current.getSede().getCodSede());
            current.getSederesponsablePK().setCodResponsable(current.getResponsable().getCodResponsable());
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("SederesponsableCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Sederesponsable) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            current.getSederesponsablePK().setCodSede(current.getSede().getCodSede());
            current.getSederesponsablePK().setCodResponsable(current.getResponsable().getCodResponsable());
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("SederesponsableUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Sederesponsable) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("SederesponsableDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Sederesponsable getSederesponsable(Modelos.SederesponsablePK id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Sederesponsable.class)
    public static class SederesponsableControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SederesponsableController controller = (SederesponsableController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "sederesponsableController");
            return controller.getSederesponsable(getKey(value));
        }

        Modelos.SederesponsablePK getKey(String value) {
            Modelos.SederesponsablePK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new Modelos.SederesponsablePK();
            key.setCodSede(values[0]);
            key.setCodResponsable(Integer.parseInt(values[1]));
            key.setFechaInicio(java.sql.Date.valueOf(values[2]));
            return key;
        }

        String getStringKey(Modelos.SederesponsablePK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getCodSede());
            sb.append(SEPARATOR);
            sb.append(value.getCodResponsable());
            sb.append(SEPARATOR);
            sb.append(value.getFechaInicio());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Sederesponsable) {
                Sederesponsable o = (Sederesponsable) object;
                return getStringKey(o.getSederesponsablePK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Sederesponsable.class.getName());
            }
        }

    }

}

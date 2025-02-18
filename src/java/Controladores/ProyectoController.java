package Controladores;

import Modelos.Proyecto;
import Controladores.util.JsfUtil;
import Controladores.util.PaginationHelper;
import Modelos.Cad;
import Modelos.Crs;
import Modelos.Odsprincipal;
import Modelos.Pais;
import Repositorio.ProyectoFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.pie.PieChartDataSet;
import org.primefaces.model.charts.pie.PieChartModel;

@Named("proyectoController")
@SessionScoped
public class ProyectoController implements Serializable {

    private Proyecto current;
    private DataModel items = null;
    @EJB
    private Repositorio.ProyectoFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private Integer anyo;
    private Pais pais;
    private List<Proyecto> listaProyectos;    
    private PieChartModel pieModel;    

    public ProyectoController() {
    }

    public Proyecto getSelected() {
        if (current == null) {
            current = new Proyecto();
            selectedItemIndex = -1;
        }
        return current;
    }

    private ProyectoFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(getProyectos().length) {

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
        current = (Proyecto) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Proyecto();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ProyectoCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Proyecto) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ProyectoUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Proyecto) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ProyectoDeleted"));
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

    public Proyecto getProyecto(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Proyecto.class)
    public static class ProyectoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProyectoController controller = (ProyectoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "proyectoController");
            return controller.getProyecto(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Proyecto) {
                Proyecto o = (Proyecto) object;
                return getStringKey(o.getCodProyecto());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Proyecto.class.getName());
            }
        }

    }
    
    public static SelectItem[] getSelectProyectos(List<Proyecto> proyectos) {        
        SelectItem[] items = new SelectItem[proyectos.size()];
        int i = 0;        
        for (Proyecto pr : proyectos) {                        
            items[i++] = new SelectItem(pr, pr.getCodigo());
        }
        return items;
    }
    
    public static SelectItem[] getSelectAnos(List<Integer> anos) {        
        SelectItem[] items = new SelectItem[anos.size()];
        int i = 0;        
        for (Integer ano : anos) {                        
            if(ano == null) continue;
            items[i++] = new SelectItem(ano, ano.toString());
        }
        return items;
    }
    
    public SelectItem[] getProyectos(){
        return getSelectProyectos(ejbFacade.findAll());
    }
    
    public SelectItem[] getAnosProyectos(){
        return getSelectAnos(ejbFacade.anosProyectos());
    }

    public Integer getAnyo() {
        return anyo;
    }

    public void setAnyo(Integer anyo) {
        this.anyo = anyo;
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public List<Proyecto> getListaProyectos() {
        return listaProyectos;
    }
    
    public void setListaProyectos(List<Proyecto> listaProyectos) {
        this.listaProyectos = listaProyectos;
    }    
    
    public void cargarProyectos(){
        if(this.anyo != null && this.pais != null) listaProyectos = ejbFacade.proyectosPorAnyoYPais(anyo, pais.getCodPais());
        else if(this.pais != null) listaProyectos = ejbFacade.proyectosPorPais(pais.getCodPais());
        else if(this.anyo != null) listaProyectos = ejbFacade.proyectosPorAnyo(anyo);
    }    
    
    public PieChartModel getPieModel() {        
        List<Number> values = new ArrayList<Number>(); //Dinero concedido
        List<String> bgColors = new ArrayList<String>();
        List<String> labels = new ArrayList<String>(); // Nombre del Proyecto
        SelectItem[] anyos = getAnosProyectos();
        Integer[] ultimosAnyos = new Integer[5];
        int resta = 1;
        for(int i = 0; i < ultimosAnyos.length; i++){
            if(anyos[anyos.length-resta-i] == null) resta++;
            ultimosAnyos[i] = (Integer) anyos[anyos.length-resta-i].getValue();
        }
        for(Integer anyo : ultimosAnyos){            
            values.add(ejbFacade.getGrafica(anyo));
            labels.add(anyo.toString()); 
            bgColors.add(new String("rgb(" + (Math.random() * 256) + "," + (Math.random() * 256) + "," + (Math.random() * 256) + ")"));
        }
        this.createPieModel(values, bgColors, labels);
        return pieModel;
    }
    
    public void createPieModel(List<Number> values, List<String> bgColors, List<String> labels) {
        pieModel = new PieChartModel();
        ChartData data = new ChartData();

        PieChartDataSet dataSet = new PieChartDataSet();
        dataSet.setData(values);
        dataSet.setBackgroundColor(bgColors);
        data.addChartDataSet(dataSet);
        data.setLabels(labels);

        pieModel.setData(data);
    }

    public String getCadNombre(Integer codPr) {
        if(codPr != null) return ejbFacade.getCad(codPr);        
        return "";
    }          
}

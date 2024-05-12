package de.hbrs.se2.control.page;


import de.hbrs.se2.control.company.CompanyService;
import de.hbrs.se2.model.company.CompanyRepository;
import de.hbrs.se2.model.jobadvertisement.AdvertisementRepository;
import de.hbrs.se2.model.user.UserRepository;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

@Service
public class Paging {
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdvertisementRepository advertisementRepository;

    @Autowired
    private CompanyService companyService;


    /**
     * @param pageSize
     * @param methodName      which method should be used for the paging
     * @param arguments       here the parameter for the method get passed
     * @param <ID>            What type is the id the entity
     * @param <K>             PagingAndSorting or a subtype
     *                        <p>
     *                        this method thakes the attribute name by which the ojects are supopoesed to be sorted
     *                        also it takes the repository that is capable of PadingAndSorting, so that it can be used genericly for every entiy type
     *                        <p>
     *                        also it only workd if the pagable parameter is the last argument
     * @param repositoryName: it must be the name of the repository bean in order to find the bean of the repository
     * @return
     */

    public <T, ID, K extends PagingAndSortingRepository<T, ID>> Iterator<List<T>> doPaging(int pageSize, String repositoryName, Order[] order, String methodName, Object... arguments) {

        return new Iterator<List<T>>() {


            final Object bean = ApplicationContextProvider.getApplicationContext().getBean(repositoryName);
            final Method method;
            private final PageInformation pageInformation = new PageInformation(0, pageSize, order);
            private final Object[] mergedArgs;
            private Pageable pageable = PageRequest.of(pageInformation.getPageNumber(), pageInformation.getPageSize(), pageInformation.getSort());
            private Page<T> page;

            {
                try {
                    Class<?>[] type = getArgumentsTypes(arguments); //{Pageable.class};
                    method = bean.getClass().getMethod(methodName, type);
                    mergedArgs = mergeArgument(pageable, arguments);
                    page = (Page<T>) method.invoke(bean, mergeArgument(pageable, arguments));

                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }


            @Override
            public boolean hasNext() {
                return pageInformation.getPageNumber() < page.getTotalPages();
            }

            @Override
            public List<T> next() {
                List<T> retrieved = page.getContent();

                setNextPage();
                return retrieved;


            }

            //getting the next page
            private void setNextPage() {


                // set the new page number
                pageInformation.setPageNumber(pageInformation.getPageNumber() + 1);
                //defining new paging information
                pageable = PageRequest.of(pageInformation.getPageNumber(), pageInformation.getPageSize(), pageInformation.getSort());
                //getting the new page
                try {
                    page = (Page<T>) method.invoke(bean, mergeArgument(pageable, arguments));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }

            private String getRepositoryInterface(K repository) {
                String className = repository.getClass().getInterfaces()[0].getSimpleName();
                return Character.toLowerCase(className.charAt(0)) + className.substring(1);
            }

            /**
             *
             * @param arguments
             * @return all the types of the arguments. also adds Pageable.class at the end
             */
            private Class<?>[] getArgumentsTypes(Object[] arguments) {
                if (arguments == null)
                    return new Class<?>[]{Pageable.class};
                Class<?>[] types = new Class[arguments.length + 1];
                for (int i = 0; i < arguments.length; i++) {
                    types[i] = arguments[i].getClass();
                }
                types[arguments.length] = Pageable.class;

                return types;
            }

            /**
             *
             * @param array1
             * @param pageable
             * @return merges the arguements with the par
             */
            public Object[] mergeArgument(Pageable pageable, Object... array1) {
                if (array1 == null)
                    return new Object[]{pageable};

                // Create a new array with the combined length
                Object[] mergedArray = new Object[array1.length + 1];
                int i = 0;
                for (Object obj : array1)
                    mergedArray[i++] = obj;
                mergedArray[i] = pageable;


                return mergedArray;
            }


        };

    }
}

@Component
class ApplicationContextProvider implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextProvider.applicationContext = applicationContext;
    }
}
package id.ac.tazkia.kinerja.aplikasikinerja.dao;

import id.ac.tazkia.kinerja.aplikasikinerja.entity.Category;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.Kpi;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.StatusKpi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface KpiDao extends PagingAndSortingRepository<Kpi, String> {
    Page<Kpi> findByStatusAndKeyResultContainingIgnoreCaseOrderByKeyResult(StatusKpi aktif, String keyresult, Pageable page);
    Page<Kpi> findByStatus(StatusKpi aktif, Pageable page);
    List<Kpi> findByStatus(StatusKpi aktif);
    List<Kpi> findByCategoryAndStatus(Category c, StatusKpi s);
}
package id.ac.tazkia.kinerja.aplikasikinerja.controller;

import id.ac.tazkia.kinerja.aplikasikinerja.constants.AktifConstants;
import id.ac.tazkia.kinerja.aplikasikinerja.dao.KpiDao;
import id.ac.tazkia.kinerja.aplikasikinerja.dao.StaffRoleDao;
import id.ac.tazkia.kinerja.aplikasikinerja.dto.StaffKpiDto;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.Kpi;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.Periode;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.StaffRole;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.StatusKpi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;
import java.util.Set;

@Controller
public class RoleController {

    @Autowired
    private StaffRoleDao staffRoleDao;

    @Autowired
    private KpiDao kpiDao;

    @GetMapping("/role/list")
    private void roleList(Model model,String role,@PageableDefault(size = 10) Pageable page) {
        if (StringUtils.hasText(role)) {
            model.addAttribute("role", role);
            model.addAttribute("roleList", staffRoleDao.findByStatusAndAndRoleNameContainingIgnoreCaseOrderByRoleName(AktifConstants.Aktif,role,page));
        } else {
            model.addAttribute("roleList", staffRoleDao.findByStatus(AktifConstants.Aktif,page));
        }
    }

    @GetMapping("role/kpi")
    private void kpiInput(@RequestParam String role, Model model) {
        StaffRole staffRole = staffRoleDao.findById(role).get();

        StaffKpiDto staffKpiDto = new StaffKpiDto();

        model.addAttribute("role", staffRole);/*mengambil data role*/

        model.addAttribute("pilihanKpi", kpiDao.findByStatus(StatusKpi.AKTIF));/*mengambil data kpi*/

        Set<Kpi> staffRoles = staffRole.getKpi();
        for (Kpi sr : staffRoles) {
        }
        model.addAttribute("kpiSekarang", staffRoles);
    }

    @PostMapping("role/kpi")
    public String proses(@RequestParam StaffRole role, @ModelAttribute @Valid StaffKpiDto s, BindingResult errors, SessionStatus status) {

        Set<StaffRole> staffRole = s.getRoles();
        System.out.println("kpiiiii : " + s);
        for (StaffRole sr : staffRole) {
            sr.setKpi(s.getKpi());

            System.out.println("isinya :" + sr);
            staffRoleDao.save(sr);
        }


        status.setComplete();
        return "redirect:list";
    }

    @GetMapping("/role/form")
    private void displayForm(Model m,@RequestParam(value="role", required = false) String role){

        m.addAttribute("role", new StaffRole());
        m.addAttribute("staffRole",staffRoleDao.findByStatus(AktifConstants.Aktif));

        if (role != null && !role.isEmpty()) {
            StaffRole staffRole = staffRoleDao.findById(role).get();
            if (staffRole != null) {
                m.addAttribute("role",staffRole);
            }
        }
    }

    @PostMapping("/role/form")
    public String proses(@ModelAttribute @Valid StaffRole staffRole, BindingResult error){
        staffRole.setStatus(AktifConstants.Aktif);
        staffRoleDao.save(staffRole);

        return "redirect:list";
    }

}

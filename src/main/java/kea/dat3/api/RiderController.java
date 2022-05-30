package kea.dat3.api;

import kea.dat3.dto.RiderRequest;
import kea.dat3.dto.RiderResponse;
import kea.dat3.services.RiderService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/riders")
public class RiderController {

    private final RiderService riderService;

    public RiderController(RiderService riderService) {
        this.riderService = riderService;
    }

    @GetMapping
    public List<RiderResponse> getRiders(Pageable pageable){
        return riderService.findAll(pageable);
    }

    @GetMapping("/search/{name}")
    public List<RiderResponse> getRidersByName(@PathVariable String name){
        return riderService.findByName(name);
    }

    @GetMapping("/{id}")
    public RiderResponse getRider(@PathVariable long id) {
        return riderService.findById(id);
    }
    @RolesAllowed({"USER","ADMIN"})
    @PostMapping
    public RiderResponse addRider(@RequestBody @Valid RiderRequest riderRequest) {
        return riderService.addRider(riderRequest);
    }
    @RolesAllowed({"USER","ADMIN"})
    @PutMapping("/{id}")
    public RiderResponse updateRider(@PathVariable long id, @RequestBody @Valid RiderRequest riderRequest) {
        return riderService.updateRider(id, riderRequest);
    }
    @RolesAllowed({"USER","ADMIN"})
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRider(@PathVariable long id) {
        riderService.deleteRider(id);
        return ResponseEntity.ok("{}"); // without this empty response, frontend error handling breaks!
    }
}

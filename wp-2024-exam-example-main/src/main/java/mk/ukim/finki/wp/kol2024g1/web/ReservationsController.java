package mk.ukim.finki.wp.kol2024g1.web;

import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.kol2024g1.model.RoomType;
import mk.ukim.finki.wp.kol2024g1.service.HotelService;
import mk.ukim.finki.wp.kol2024g1.service.ReservationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@AllArgsConstructor
@RequestMapping({"/", "/reservations"})
public class ReservationsController {
    private final HotelService hotelService;
    private final ReservationService reservationService;

    /**
     * This method should use the "list.html" template to display all reservations.
     * The method should be mapped on paths '/' and '/reservations'.
     * The arguments that this method takes are optional and can be 'null'.
     * In the case when the arguments are not passed (both are 'null') all reservations should be displayed.
     * If some, or all of the arguments are not 'null', the filtered reservations that are the result of the call
     * listAll method from the ReservationService should be displayed.
     * If you want to return a paginated result, you should also pass the page number and the page size as arguments.
     *
     * @param guestName
     * @param roomType
     * @param hotel
     * @param pageNum
     * @param pageSize
     * @return The view "list.html".
     */
    @RequestMapping
    public String listAll(@RequestParam(required = false)String guestName,
                          @RequestParam(required = false)RoomType roomType,
                          @RequestParam(required = false)Long hotel,
                          @RequestParam(defaultValue = "1")Integer pageNum,
                          @RequestParam(defaultValue = "10")Integer pageSize,
                          Model model) {
        model.addAttribute("reservations", reservationService.findPage(guestName, roomType, hotel, pageNum - 1,pageSize));
        model.addAttribute("hotels", hotelService.listAll());
        model.addAttribute("roomTypes", RoomType.values());
        return "list";
    }

    /**
     * This method should display the "form.html" template.
     * The method should be mapped on path '/reservations/add'.
     *
     * @return The view "form.html".
     */
    @RequestMapping("/add")
    public String showAdd(Model model) {
        model.addAttribute("hotels", hotelService.listAll());
        model.addAttribute("roomTypes", RoomType.values());
        return "form";
    }

    /**
     * This method should display the "form.html" template.
     * However, in this case, all 'input' elements should be filled with the appropriate value for the reservations that is updated.
     * The method should be mapped on path '/reservations/edit/[id]'.
     *
     * @return The view "form.html".
     */
    @RequestMapping("/edit/{id}")
    public String showEdit(@PathVariable Long id, Model model) {
        model.addAttribute("reservation", reservationService.findById(id));
        model.addAttribute("hotels", hotelService.listAll());
        model.addAttribute("roomTypes", RoomType.values());
        return "form";
    }

    /**
     * This method should create a reservation given the arguments it takes.
     * The method should be mapped on path '/reservations'.
     * After the reservation is created, all reservations should be displayed.
     *
     * @return The view "list.html".
     */
    @PostMapping
    public String create(@RequestParam(required = false)String guestName,
                         @RequestParam(required = false)LocalDate dateCreated,
                         @RequestParam(required = false)Integer daysOfStay,
                         @RequestParam(required = false)RoomType roomType,
                         @RequestParam(required = false)Long hotelId) {
        reservationService.create(guestName, dateCreated, daysOfStay, roomType, hotelId);
        return "redirect:/reservations";
    }

    /**
     * This method should update a reservation given the arguments it takes.
     * The method should be mapped on path '/reservations/[id]'.
     * After the reservation is updated, all reservations should be displayed.
     *
     * @return The view "list.html".
     */
    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam(required = false)String guestName,
                         @RequestParam(required = false)LocalDate dateCreated,
                         @RequestParam(required = false)Integer daysOfStay,
                         @RequestParam(required = false)RoomType roomType,
                         @RequestParam(required = false)Long hotelId) {
        reservationService.update(id, guestName, dateCreated, daysOfStay, roomType, hotelId);
        return "redirect:/reservations";
    }

    /**
     * This method should delete the reservation that has the appropriate identifier.
     * The method should be mapped on path '/reservations/delete/[id]'.
     * After the reservation is deleted, all reservations should be displayed.
     *
     * @return The view "list.html".
     */
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        reservationService.delete(id);
        return "redirect:/reservations";
    }

    /**
     * This method should implement the logic for extending the duration,
     * by adding one day to the daysOfStay.
     * The method should be mapped on path '/reservations/extend/[id]'.
     * After the operation, all reservations should be displayed.
     *
     * @return The view "list.html".
     */
    @PostMapping("/extend/{id}")
    public String extend(@PathVariable Long id) {
        reservationService.extendStay(id);
        return "redirect:/reservations";
    }
}

# reditt
Reditt application building using spring boot framework

Most used Annotations:
@AllArgsConstructor
@NoArgsConstructor
@Service
@Data
@Autowired
@Transactional
@Entity
@Builder
@Async
@EnableAsync
@RequestMapping
@PostMapping
@GetMapping
@NotBlank


MVC Architecture:
SpringBoot application used spring framework [Core, JPA, AOP ,MVC , DI]

Request goes to controller where we point to get the logic from service which has map class as data transfer object[DTO] and used the model to implement the service details using database data.

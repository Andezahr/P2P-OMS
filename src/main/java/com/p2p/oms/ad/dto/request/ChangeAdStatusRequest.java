import jakarta.validation.constraints.NotNull;

public record ChangeAdStatusRequest(

        @NotNull
        AdStatus status
) {
}
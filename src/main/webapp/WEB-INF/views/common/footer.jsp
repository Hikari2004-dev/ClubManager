<%@ page contentType="text/html;charset=UTF-8" language="java" %>

    <!-- Footer -->
    <footer class="py-3 mt-auto">
        <div class="container text-center">
            <span class="text-muted">
                &copy; 2026 Club Manager. All rights reserved.
            </span>
        </div>
    </footer>
    
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- Custom Scripts -->
    <script>
        // Auto-hide alerts after 5 seconds
        document.querySelectorAll('.alert-dismissible').forEach(function(alert) {
            setTimeout(function() {
                var bsAlert = new bootstrap.Alert(alert);
                bsAlert.close();
            }, 5000);
        });
        
        // Confirm delete
        function confirmDelete(message) {
            return confirm(message || 'Bạn có chắc chắn muốn xóa?');
        }
    </script>
</body>
</html>

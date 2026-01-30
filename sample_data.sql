-- =============================================
-- SCRIPT TẠO DỮ LIỆU MẪU CHO HỆ THỐNG QUẢN LÝ CLB
-- =============================================

USE CAULACBO;

-- =============================================
-- 1. TẠO USERS VỚI CÁC ROLE KHÁC NHAU
-- Mật khẩu cho tất cả user là: 123456
-- =============================================

-- Admin (Quản trị viên hệ thống)
INSERT INTO `users` (`full_name`, `email`, `username`, `password_hash`, `phone`, `class_name`, `student_code`, `is_active`) 
VALUES ('Nguyễn Văn Admin', 'admin@school.edu.vn', 'admin', '123456', '0901234567', NULL, NULL, 1);

-- Teachers (Giáo viên)
INSERT INTO `users` (`full_name`, `email`, `username`, `password_hash`, `phone`, `class_name`, `student_code`, `is_active`) 
VALUES ('Trần Thị Giáo Viên', 'giaovien1@school.edu.vn', 'teacher1', '123456', '0912345678', NULL, NULL, 1);

INSERT INTO `users` (`full_name`, `email`, `username`, `password_hash`, `phone`, `class_name`, `student_code`, `is_active`) 
VALUES ('Lê Văn Thầy', 'giaovien2@school.edu.vn', 'teacher2', '123456', '0923456789', NULL, NULL, 1);

-- Club Leaders (Chủ nhiệm CLB)
INSERT INTO `users` (`full_name`, `email`, `username`, `password_hash`, `phone`, `class_name`, `student_code`, `is_active`) 
VALUES ('Phạm Minh Chủ Nhiệm', 'chunhiem1@school.edu.vn', 'leader1', '123456', '0934567890', 'CNTT-K20', 'SV001', 1);

INSERT INTO `users` (`full_name`, `email`, `username`, `password_hash`, `phone`, `class_name`, `student_code`, `is_active`) 
VALUES ('Hoàng Thị Leader', 'chunhiem2@school.edu.vn', 'leader2', '123456', '0945678901', 'QTKD-K21', 'SV002', 1);

-- Members (Thành viên thường)
INSERT INTO `users` (`full_name`, `email`, `username`, `password_hash`, `phone`, `class_name`, `student_code`, `is_active`) 
VALUES ('Nguyễn Thành Viên A', 'member1@school.edu.vn', 'member1', '123456', '0956789012', 'CNTT-K21', 'SV003', 1);

INSERT INTO `users` (`full_name`, `email`, `username`, `password_hash`, `phone`, `class_name`, `student_code`, `is_active`) 
VALUES ('Trần Thị Thành Viên B', 'member2@school.edu.vn', 'member2', '123456', '0967890123', 'CNTT-K21', 'SV004', 1);

INSERT INTO `users` (`full_name`, `email`, `username`, `password_hash`, `phone`, `class_name`, `student_code`, `is_active`) 
VALUES ('Lê Văn Thành Viên C', 'member3@school.edu.vn', 'member3', '123456', '0978901234', 'QTKD-K20', 'SV005', 1);

INSERT INTO `users` (`full_name`, `email`, `username`, `password_hash`, `phone`, `class_name`, `student_code`, `is_active`) 
VALUES ('Phạm Thị Sinh Viên D', 'member4@school.edu.vn', 'member4', '123456', '0989012345', 'KT-K21', 'SV006', 1);

INSERT INTO `users` (`full_name`, `email`, `username`, `password_hash`, `phone`, `class_name`, `student_code`, `is_active`) 
VALUES ('Hoàng Văn Sinh Viên E', 'member5@school.edu.vn', 'member5', '123456', '0990123456', 'NN-K20', 'SV007', 1);

-- =============================================
-- 2. GÁN ROLE CHO USERS
-- Roles: 1=admin, 2=teacher, 3=club_leader, 4=member
-- =============================================

-- Admin role
INSERT INTO `user_roles` (`user_id`, `role_id`) 
SELECT id, 1 FROM users WHERE username = 'admin';

-- Teacher roles
INSERT INTO `user_roles` (`user_id`, `role_id`) 
SELECT id, 2 FROM users WHERE username IN ('teacher1', 'teacher2');

-- Club Leader roles
INSERT INTO `user_roles` (`user_id`, `role_id`) 
SELECT id, 3 FROM users WHERE username IN ('leader1', 'leader2');

-- Member roles
INSERT INTO `user_roles` (`user_id`, `role_id`) 
SELECT id, 4 FROM users WHERE username IN ('member1', 'member2', 'member3', 'member4', 'member5');

-- Club leaders cũng là thành viên
INSERT INTO `user_roles` (`user_id`, `role_id`) 
SELECT id, 4 FROM users WHERE username IN ('leader1', 'leader2');

-- =============================================
-- 3. TẠO CÁC CÂU LẠC BỘ MẪU
-- =============================================

INSERT INTO `clubs` (`name`, `description`, `category`, `supervisor_user_id`, `status`) 
VALUES (
    'CLB Lập Trình', 
    'Câu lạc bộ dành cho các bạn đam mê lập trình, phát triển phần mềm và công nghệ thông tin.',
    'Công nghệ',
    (SELECT id FROM users WHERE username = 'teacher1'),
    'active'
);

INSERT INTO `clubs` (`name`, `description`, `category`, `supervisor_user_id`, `status`) 
VALUES (
    'CLB Tiếng Anh', 
    'Nơi giao lưu, học tập và rèn luyện kỹ năng tiếng Anh cho sinh viên.',
    'Ngoại ngữ',
    (SELECT id FROM users WHERE username = 'teacher2'),
    'active'
);

INSERT INTO `clubs` (`name`, `description`, `category`, `supervisor_user_id`, `status`) 
VALUES (
    'CLB Văn Nghệ', 
    'Câu lạc bộ ca hát, nhảy múa và các hoạt động văn nghệ.',
    'Văn hóa - Nghệ thuật',
    NULL,
    'active'
);

INSERT INTO `clubs` (`name`, `description`, `category`, `supervisor_user_id`, `status`) 
VALUES (
    'CLB Bóng Đá', 
    'Câu lạc bộ thể thao bóng đá nam sinh viên.',
    'Thể thao',
    NULL,
    'active'
);

-- =============================================
-- 4. THÊM THÀNH VIÊN VÀO CÂU LẠC BỘ
-- club_role: president, vice_president, secretary, member
-- status: pending, approved, rejected, left
-- =============================================

-- CLB Lập Trình (club_id = 1)
INSERT INTO `club_memberships` (`club_id`, `user_id`, `club_role`, `status`, `approved_by`, `approved_at`)
SELECT 1, id, 'president', 'approved', (SELECT id FROM users WHERE username = 'admin'), NOW()
FROM users WHERE username = 'leader1';

INSERT INTO `club_memberships` (`club_id`, `user_id`, `club_role`, `status`, `approved_by`, `approved_at`)
SELECT 1, id, 'member', 'approved', (SELECT id FROM users WHERE username = 'leader1'), NOW()
FROM users WHERE username = 'member1';

INSERT INTO `club_memberships` (`club_id`, `user_id`, `club_role`, `status`, `approved_by`, `approved_at`)
SELECT 1, id, 'member', 'approved', (SELECT id FROM users WHERE username = 'leader1'), NOW()
FROM users WHERE username = 'member2';

INSERT INTO `club_memberships` (`club_id`, `user_id`, `club_role`, `status`)
SELECT 1, id, 'member', 'pending'
FROM users WHERE username = 'member3';

-- CLB Tiếng Anh (club_id = 2)
INSERT INTO `club_memberships` (`club_id`, `user_id`, `club_role`, `status`, `approved_by`, `approved_at`)
SELECT 2, id, 'president', 'approved', (SELECT id FROM users WHERE username = 'admin'), NOW()
FROM users WHERE username = 'leader2';

INSERT INTO `club_memberships` (`club_id`, `user_id`, `club_role`, `status`, `approved_by`, `approved_at`)
SELECT 2, id, 'vice_president', 'approved', (SELECT id FROM users WHERE username = 'leader2'), NOW()
FROM users WHERE username = 'member4';

INSERT INTO `club_memberships` (`club_id`, `user_id`, `club_role`, `status`, `approved_by`, `approved_at`)
SELECT 2, id, 'member', 'approved', (SELECT id FROM users WHERE username = 'leader2'), NOW()
FROM users WHERE username = 'member5';

-- =============================================
-- 5. TẠO SỰ KIỆN MẪU
-- =============================================

INSERT INTO `events` (`club_id`, `title`, `description`, `location`, `starts_at`, `ends_at`, `capacity`, `allow_external`, `status`, `created_by`)
SELECT 
    1, 
    'Workshop: Giới thiệu về AI', 
    'Buổi workshop giới thiệu về trí tuệ nhân tạo và ứng dụng trong cuộc sống.',
    'Hội trường A1',
    DATE_ADD(NOW(), INTERVAL 7 DAY),
    DATE_ADD(NOW(), INTERVAL 7 DAY) + INTERVAL 3 HOUR,
    50,
    1,
    'published',
    id
FROM users WHERE username = 'leader1';

INSERT INTO `events` (`club_id`, `title`, `description`, `location`, `starts_at`, `ends_at`, `capacity`, `allow_external`, `status`, `created_by`)
SELECT 
    1, 
    'Hackathon 2026', 
    'Cuộc thi lập trình 24h với các giải thưởng hấp dẫn.',
    'Tòa nhà công nghệ',
    DATE_ADD(NOW(), INTERVAL 14 DAY),
    DATE_ADD(NOW(), INTERVAL 15 DAY),
    100,
    1,
    'published',
    id
FROM users WHERE username = 'leader1';

INSERT INTO `events` (`club_id`, `title`, `description`, `location`, `starts_at`, `ends_at`, `capacity`, `allow_external`, `status`, `created_by`)
SELECT 
    2, 
    'English Speaking Contest', 
    'Cuộc thi hùng biện tiếng Anh dành cho sinh viên.',
    'Hội trường lớn',
    DATE_ADD(NOW(), INTERVAL 10 DAY),
    DATE_ADD(NOW(), INTERVAL 10 DAY) + INTERVAL 4 HOUR,
    30,
    0,
    'published',
    id
FROM users WHERE username = 'leader2';

-- =============================================
-- HOÀN TẤT
-- =============================================
SELECT 'Đã tạo dữ liệu mẫu thành công!' AS message;

-- Hiển thị danh sách users và roles
SELECT u.username, u.full_name, GROUP_CONCAT(r.display_name) AS roles
FROM users u
LEFT JOIN user_roles ur ON u.id = ur.user_id
LEFT JOIN roles r ON ur.role_id = r.id
GROUP BY u.id, u.username, u.full_name
ORDER BY u.id;

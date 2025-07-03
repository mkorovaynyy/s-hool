-- 2. Имена студентов уникальны и не null
ALTER TABLE student
    ALTER COLUMN name SET NOT NULL,
  ADD CONSTRAINT unique_name UNIQUE (name);

-- 3. Уникальность пары "название-цвет" для факультетов
ALTER TABLE faculty
    ADD CONSTRAINT unique_name_color UNIQUE (name, color);

-- 4. Значение возраста по умолчанию (20 лет)
ALTER TABLE student
    ALTER COLUMN age SET DEFAULT 20;
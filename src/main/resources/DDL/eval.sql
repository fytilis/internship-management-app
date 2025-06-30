
ALTER TABLE evaluation
  DROP INDEX `UK_2d037orv4w1m4oyy1ghjva7ju`;


ALTER TABLE evaluation
  ADD CONSTRAINT `UK_EVAL_STU_POS_TYPE`
    UNIQUE (`student_id`, `position_id`, `evaluator_type`);

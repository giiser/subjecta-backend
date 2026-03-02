# 🏗 System Architecture

```
                         ┌────────────────────┐
                         │    Contentful CMS  │
                         │ (Subjects, Topics, │
                         │  Lessons, Quizzes) │
                         └─────────┬──────────┘
                                   │
                            CMS REST API
                                   │
                         ┌─────────▼──────────┐
                         │  ContentfulClient  │
                         │  (WebClient +      │
                         │   Caffeine Cache)  │
                         └─────────┬──────────┘
                                   │
                 ┌─────────────────▼─────────────────┐
                 │           Service Layer           │
                 │-----------------------------------│
                 │ • AuthService                     │
                 │ • LessonService                   │
                 │ • QuizService                     │
                 │ • ProgressService                 │
                 └─────────┬───────────┬─────────────┘
                           │           │
                           │           │
                 ┌─────────▼───┐   ┌───▼─────────┐
                 │ Repositories│   │ JWT Service  │
                 │ (JPA)       │   │ & Security   │
                 └──────┬──────┘   └────┬─────────┘
                        │               │
                ┌───────▼────────┐      │
                │   PostgreSQL   │      │
                │   (User Data   │      │
                │   + Progress)  │      │
                └────────────────┘      │
                                        │
                               ┌────────▼────────┐
                               │  Spring Security │
                               │  JWT Filter      │
                               └────────┬────────┘
                                        │
                                 ┌──────▼──────┐
                                 │ Controllers │
                                 │  REST API   │
                                 └─────────────┘
```

---

## 🔄 Request Flow — Example: Access Lesson

```
Client
  ↓
JWT Authentication Filter
  ↓
LessonController
  ↓
LessonService
  ↓
ProgressService (check unlock rules)
  ↓
ContentfulClient (cached CMS call)
  ↓
Return LessonWithAccessDto { locked: true/false }
```

---

## 🔐 Authentication Flow

```
Login Request
  ↓
AuthController
  ↓
AuthService
  ↓
Generate Access Token + Refresh Token
  ↓
Return tokens
```

**Subsequent Requests:**

```
Client → Bearer Token → JWT Filter → SecurityContext
```

---

## 📊 Progress Calculation Flow

```
User completes quiz
  ↓
QuizService saves result
  ↓
ProgressService marks lesson complete
  ↓
Topic & Subject percentage recalculated dynamically
```

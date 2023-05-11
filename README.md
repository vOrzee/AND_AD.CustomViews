# CustomViews
Анимации и создание собственных View

## Задача. Smart StatsView

### Описание

На текущий момент наша Custom View принимает в качестве данных доли:

```kotlin
findViewById<StatsView>(R.id.stats).data = listOf(
    0.25F,
    0.25F,
    0.25F,
    0.25F,
)
```

Что в сумме даёт нам картинку из лекции:

![](https://github.com/netology-code/andad-homeworks/blob/master/05_views/pic/diagram.png?raw=true)

**Q**: что мы хотим?

**A**: мы хотим, чтобы `StatsView` принимала на вход данные, по которым сама рассчитывала проценты:

```kotlin
findViewById<StatsView>(R.id.stats).data = listOf(
    500F,
    500F,
    500F,
    500F,
)
```

Что должно давать такую же картинку, т. к. `SmartStatsView` просуммирует все данные и определит, что каждый элемент — это ровно 25 %: 

![](https://github.com/netology-code/andad-homeworks/blob/master/05_views/pic/diagram.png?raw=true)


## Задача. Dot

### Описание

В нашей реализации есть один не очень приятный нюанс:

![](https://github.com/netology-code/andad-homeworks/blob/master/05_views/pic/dot.png?raw=true)

Исправьте реализацию таким образом, чтобы мы из картинки «Как сейчас» получили картинку «Как должно быть».


## Задача. Not Filled*

### Описание

Если вы реализовали предыдущую задачу, то заполняемость вашего графика всегда 100 %.

Мы хотим сделать так, чтобы можно было иметь и незаполненную часть:

![](https://github.com/netology-code/andad-homeworks/blob/master/05_views/pic/notfilled.png?raw=true)

## Задача. Rotation

### Описание

Необходимо дополнить реализованную на лекции анимацию поворотом на 360 градусов:

<img src="https://user-images.githubusercontent.com/13727567/142734792-c71faf9b-6014-407d-8257-2193cfa70fa2.gif" width="25%" height="25%"/>

Обратите внимание, что текст остаётся неподвижным.

Для решения задачи подумайте:
1. Какой параметр отвечает за прогресс анимации.
2. Как перевести его в градусы.
3. Какой параметр отвечает за начальный сдвиг при отрисовке каждой дуги.

## Задача. Sequential*

### Описание

На лекции мы реализовали параллельное заполнение данными:

![](https://github.com/netology-code/andad-homeworks/blob/master/06_animations/pic/parallel.png?raw=true)

Сейчас же мы хотим сделать последовательное:

![](https://github.com/netology-code/andad-homeworks/blob/master/06_animations/pic/sequential.png?raw=true)


## Задача. Attributes*

### Описание

Если вы выполнили предыдущую задачу, то у вас получилось две реализации заполнения данными:
1. Параллельная.
2. Последовательная.

Сделайте так, чтобы тип заполнения можно было выбирать через XML. Используйте для этого `format="enum"`. В качестве примера реализации смотрите статью на [developer.android.com](https://developer.android.com/training/custom-views/create-view).


## Задача. Bidirectional*

### Описание

Предлагаем дополнить параллельное заполнение данными следующим режимом:

![ezgif com-gif-maker](https://user-images.githubusercontent.com/13727567/132138140-06b4bd35-2df8-49e1-9a77-2630ed94adf2.gif)


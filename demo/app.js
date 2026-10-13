/* Demo interactiva: datos ficticios aislados en el navegador. */
const STORAGE_KEY = "hostal-delfines-demo-v1";
const ROUTES = ["inicio", "reservas", "habitaciones", "clientes", "ventas"];
const STATUS = { pendiente: "Pendiente", confirmada: "Confirmada", finalizada: "Finalizada", cancelada: "Cancelada" };
const main = document.getElementById("main");
const modal = document.getElementById("modal");
const toastStack = document.getElementById("toastStack");
const sidebar = document.getElementById("sidebar");
const menuButton = document.getElementById("menuButton");
let state = loadState();
let cart = {};
let reservationFilter = { search: "", status: "todas" };
let clientSearch = "";

function icon(name, extra = "") {
  return `<svg class="icon ${extra}" aria-hidden="true"><use href="#i-${name}"></use></svg>`;
}
function escapeHtml(value) {
  return String(value ?? "").replace(/[&<>"']/g, character =>
    ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;" })[character]);
}
function localDate(date = new Date()) {
  const d = new Date(date);
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, "0")}-${String(d.getDate()).padStart(2, "0")}`;
}
function addDays(days) {
  const d = new Date();
  d.setHours(12, 0, 0, 0);
  d.setDate(d.getDate() + days);
  return localDate(d);
}
function dateTime(date, time) { return new Date(`${date}T${time}:00`); }
function formatDate(date, options = { day: "numeric", month: "short" }) {
  return new Intl.DateTimeFormat("es-PE", options).format(new Date(`${date}T12:00:00`));
}
function currency(amount) {
  return new Intl.NumberFormat("es-PE", { style: "currency", currency: "PEN" }).format(Number(amount) || 0);
}
function seedState() {
  return {
    version: 1,
    seededAt: localDate(),
    rooms: [
      { id: 101, beds: 1, window: true, active: true },
      { id: 102, beds: 2, window: false, active: true },
      { id: 103, beds: 1, window: false, active: true },
      { id: 104, beds: 2, window: true, active: true }
    ],
    clients: [
      { id: 1, document: "00000001", name: "María Quispe" },
      { id: 2, document: "00000002", name: "Diego Ramos" },
      { id: 3, document: "00000003", name: "Ana Torres" },
      { id: 4, document: "00000004", name: "Luis Vega" }
    ],
    reservations: [
      { id: 1001, clientId: 1, roomId: 101, date: addDays(1), time: "14:00", hours: 3, guests: 1, cash: 25, yape: 0, status: "confirmada" },
      { id: 1002, clientId: 2, roomId: 102, date: addDays(2), time: "15:00", hours: 24, guests: 2, cash: 20, yape: 50, status: "pendiente" },
      { id: 1003, clientId: 3, roomId: 103, date: addDays(3), time: "10:00", hours: 24, guests: 1, cash: 40, yape: 0, status: "confirmada" },
      { id: 1004, clientId: 4, roomId: 104, date: addDays(-1), time: "14:00", hours: 3, guests: 2, cash: 25, yape: 0, status: "finalizada" }
    ],
    products: [
      { id: 1, name: "Agua embotellada", price: 3, stock: 12 },
      { id: 2, name: "Cepillo dental", price: 5, stock: 8 },
      { id: 3, name: "Bebida natural", price: 6, stock: 10 }
    ],
    sales: [
      { id: 501, date: addDays(-1), items: [{ productId: 1, name: "Agua embotellada", quantity: 2, price: 3 }], total: 6 }
    ]
  };
}
function loadState() {
  try {
    const saved = JSON.parse(localStorage.getItem(STORAGE_KEY));
    const age = saved?.seededAt ? Date.now() - new Date(`${saved.seededAt}T12:00:00`).getTime() : Infinity;
    if (saved && saved.version === 1 && age < 14 * 86400000
        && ["rooms", "clients", "reservations", "products", "sales"].every(key => Array.isArray(saved[key]))) return saved;
  } catch (_) {
    // La demo continúa en memoria si el navegador bloquea el almacenamiento.
  }
  return seedState();
}
function saveState() {
  try { localStorage.setItem(STORAGE_KEY, JSON.stringify(state)); }
  catch (_) { toast("No se pudieron guardar los cambios en este navegador.", "error"); }
}
function route() {
  const requested = location.hash.replace(/^#/, "");
  return ROUTES.includes(requested) ? requested : "inicio";
}
function go(target) {
  if (location.hash === `#${target}`) render();
  else location.hash = target;
}
function client(id) { return state.clients.find(item => item.id === Number(id)); }
function room(id) { return state.rooms.find(item => item.id === Number(id)); }
function priceFor(roomItem, hours) {
  if (!roomItem) return 0;
  if (Number(hours) === 3) return roomItem.window ? 25 : 20;
  if (Number(hours) === 24) return roomItem.beds === 2 ? 70 : roomItem.window ? 50 : 40;
  return 0;
}
function isActive(reservation) { return reservation.status !== "cancelada" && reservation.status !== "finalizada"; }
function available(roomId, date, time, hours, excludedId = 0) {
  const chosenRoom = room(roomId);
  if (!chosenRoom || !chosenRoom.active) return false;
  const start = dateTime(date, time).getTime();
  const end = start + Number(hours) * 3600000;
  if (!Number.isFinite(start) || !Number.isFinite(end)) return false;
  return !state.reservations.some(item => {
    if (!isActive(item) || item.roomId !== Number(roomId) || item.id === Number(excludedId)) return false;
    const otherStart = dateTime(item.date, item.time).getTime();
    return start < otherStart + item.hours * 3600000 && otherStart < end;
  });
}
function nextReservation(roomId) {
  const now = Date.now();
  return state.reservations
    .filter(item => isActive(item) && item.roomId === roomId && dateTime(item.date, item.time).getTime() + item.hours * 3600000 > now)
    .sort((a, b) => dateTime(a.date, a.time) - dateTime(b.date, b.time))[0];
}
function statusBadge(status) {
  return `<span class="status status--${escapeHtml(status)}"><span class="status-dot"></span>${STATUS[status] || escapeHtml(status)}</span>`;
}
function pageHeader(kicker, title, description, action = "") {
  return `<header class="page-header"><div><span class="eyebrow">${kicker}</span><h1>${title}</h1><p>${description}</p></div>${action ? `<div class="page-action">${action}</div>` : ""}</header>`;
}
function button(label, action, value = "", variant = "primary", iconName = "plus") {
  return `<button type="button" class="button button--${variant}" data-action="${action}" data-id="${escapeHtml(value)}">${icon(iconName)}<span>${label}</span></button>`;
}
function empty(title, copy, iconName = "info") {
  return `<div class="empty-state">${icon(iconName)}<h3>${title}</h3><p>${copy}</p></div>`;
}
function toast(message, kind = "success") {
  const item = document.createElement("div");
  item.className = `toast toast--${kind}`;
  item.setAttribute("role", kind === "error" ? "alert" : "status");
  item.innerHTML = `${icon(kind === "error" ? "info" : "check")}<span>${escapeHtml(message)}</span>`;
  toastStack.append(item);
  setTimeout(() => item.remove(), 4200);
}
function render() {
  const current = route();
  document.querySelectorAll(".nav-link").forEach(link => {
    const active = link.dataset.route === current;
    link.classList.toggle("is-active", active);
    if (active) link.setAttribute("aria-current", "page");
    else link.removeAttribute("aria-current");
  });
  document.getElementById("topbarTitle").textContent = {
    inicio: "Vista general", reservas: "Reservas", habitaciones: "Habitaciones", clientes: "Clientes", ventas: "Ventas"
  }[current];
  main.innerHTML = ({ inicio: renderHome, reservas: renderReservations, habitaciones: renderRooms, clientes: renderClients, ventas: renderSales })[current]();
  if (current === "reservas") renderReservationRows();
  if (current === "clientes") renderClientRows();
  closeMenu();
}
function renderHome() {
  const upcoming = state.reservations
    .filter(item => isActive(item) && dateTime(item.date, item.time).getTime() >= Date.now())
    .sort((a, b) => dateTime(a.date, a.time) - dateTime(b.date, b.time));
  const paid = state.reservations.filter(item => item.date === localDate() && item.status !== "cancelada")
    .reduce((sum, item) => sum + item.cash + item.yape, 0);
  return `
    <section class="welcome">
      <div class="welcome-copy"><span class="eyebrow eyebrow--light">HOSTAL DELFINES · RECEPCIÓN</span>
        <h1>La jornada empieza<br>con una vista clara.</h1>
        <p>Consulta las próximas estancias y continúa la operación desde un solo lugar.</p>
        <button type="button" class="text-link" data-action="navigate" data-id="reservas">Ir a reservas ${icon("arrow")}</button>
      </div>
      <div class="welcome-art" aria-hidden="true"><span class="sun"></span><span class="wave wave--one"></span><span class="wave wave--two"></span><span class="wave wave--three"></span><span class="horizon-label">COSTA · CALMA · HOSPITALIDAD</span></div>
    </section>
    <div class="overview">
      <div class="overview-primary"><span class="eyebrow">PRÓXIMAS ESTANCIAS</span><strong>${upcoming.length}</strong><span>reservas por atender</span></div>
      <div class="overview-stat"><span>Habitaciones</span><strong>${state.rooms.filter(item => item.active).length}</strong><small>habilitadas</small></div>
      <div class="overview-stat"><span>Clientes</span><strong>${state.clients.length}</strong><small>registrados</small></div>
      <div class="overview-stat"><span>Pagos de hoy</span><strong>${currency(paid)}</strong><small>reservas</small></div>
    </div>
    <div class="home-layout">
      <section class="panel upcoming-panel"><div class="panel-heading"><div><span class="eyebrow">RECEPCIÓN</span><h2>Lo que viene</h2></div><a class="inline-link" href="#reservas">Ver todas ${icon("arrow")}</a></div>
        ${upcoming.length ? `<div class="upcoming-list">${upcoming.slice(0, 3).map(item => `<div class="upcoming-item"><div class="day-block"><strong>${formatDate(item.date, {day:"2-digit"})}</strong><small>${formatDate(item.date, {month:"short"}).toUpperCase()}</small></div><div><strong>${escapeHtml(client(item.clientId)?.name || "Cliente")}</strong><span>Habitación ${item.roomId} · ${item.time} · ${item.hours} h</span></div>${statusBadge(item.status)}</div>`).join("")}</div>` : empty("Todo al día", "No hay reservas próximas.")}</section>
      <section class="guide-panel"><span class="eyebrow">RECORRIDO SUGERIDO</span><h2>Explora el flujo<br>de una estancia.</h2><p>Revisa habitaciones, elige un cliente y registra una reserva. Los cambios quedarán solo en tu navegador.</p>
        <div class="guide-steps"><button data-action="navigate" data-id="habitaciones"><b>01</b><span>Ver habitaciones</span>${icon("arrow")}</button><button data-action="navigate" data-id="clientes"><b>02</b><span>Consultar clientes</span>${icon("arrow")}</button><button data-action="new-reservation"><b>03</b><span>Crear una reserva</span>${icon("arrow")}</button></div>
      </section>
    </div>`;
}
function renderReservations() {
  return `${pageHeader("RECEPCIÓN / RESERVAS", "Reservas", "Organiza llegadas, horarios y pagos desde una misma vista.", button("Nueva reserva", "new-reservation"))}
    <section class="panel list-panel">
      <div class="toolbar"><div class="toolbar-copy"><span class="eyebrow">AGENDA</span><h2>Estancias registradas</h2></div>
        <div class="filters"><label class="sr-only" for="reservationSearch">Buscar reserva</label><input id="reservationSearch" type="search" placeholder="Buscar cliente o habitación" value="${escapeHtml(reservationFilter.search)}" autocomplete="off">
        <label class="sr-only" for="reservationStatus">Filtrar por estado</label><select id="reservationStatus"><option value="todas">Todos los estados</option>${Object.entries(STATUS).map(([value, label]) => `<option value="${value}" ${reservationFilter.status === value ? "selected" : ""}>${label}</option>`).join("")}</select></div></div>
      <div id="reservationRows"></div>
    </section>`;
}
function renderReservationRows() {
  const target = document.getElementById("reservationRows");
  if (!target) return;
  const query = reservationFilter.search.trim().toLocaleLowerCase("es");
  const rows = state.reservations.filter(item => {
    const name = client(item.clientId)?.name || "";
    return (reservationFilter.status === "todas" || item.status === reservationFilter.status)
      && (!query || `${name} ${item.roomId} ${item.id}`.toLocaleLowerCase("es").includes(query));
  }).sort((a, b) => dateTime(b.date, b.time) - dateTime(a.date, a.time));
  target.innerHTML = rows.length ? `<div class="reservation-list"><div class="list-labels"><span>ESTANCIA</span><span>HABITACIÓN</span><span>IMPORTE</span><span>ESTADO</span><span></span></div>
    ${rows.map(item => `<article class="reservation-row"><div class="reservation-person"><strong>${escapeHtml(client(item.clientId)?.name || "Cliente eliminado")}</strong><small>#${item.id} · ${formatDate(item.date, {day:"numeric",month:"long",year:"numeric"})} a las ${item.time}</small></div>
      <div class="reservation-cell"><small class="mobile-label">HABITACIÓN</small><strong>${item.roomId}</strong><small>${item.hours} horas · ${item.guests} huésped${item.guests === 1 ? "" : "es"}</small></div>
      <div class="reservation-cell"><small class="mobile-label">IMPORTE</small><strong>${currency(priceFor(room(item.roomId), item.hours))}</strong><small>Pagado ${currency(item.cash + item.yape)}</small></div>
      <div class="reservation-cell"><small class="mobile-label">ESTADO</small>${statusBadge(item.status)}</div>
      <div class="row-actions"><button type="button" class="row-button" data-action="edit-reservation" data-id="${item.id}" aria-label="Editar reserva ${item.id}">Editar</button>${isActive(item) ? `<button type="button" class="row-button row-button--danger" data-action="cancel-reservation" data-id="${item.id}" aria-label="Cancelar reserva ${item.id}">Cancelar</button>` : ""}</div></article>`).join("")}</div>` : empty("No encontramos reservas", "Prueba otro filtro o crea una reserva nueva.", "calendar");
}
function renderRooms() {
  const now = Date.now();
  return `${pageHeader("RECEPCIÓN / HABITACIONES", "Habitaciones", "Consulta capacidad, tarifa y próximas estancias.")}
    <div class="rooms-intro"><span class="eyebrow">MAPA DE ALOJAMIENTO</span><p>Selecciona una habitación para iniciar una reserva. La disponibilidad se comprobará al elegir fecha y horario.</p><span>${state.rooms.length} habitaciones</span></div>
    <div class="room-grid">${state.rooms.map((item, index) => {
      const next = nextReservation(item.id);
      const occupied = next && dateTime(next.date, next.time).getTime() <= now;
      const label = occupied ? "Ocupada ahora" : next ? "Próxima reserva" : "Sin reservas próximas";
      return `<article class="room-card ${index === 0 ? "room-card--feature" : ""}"><div class="room-card-top"><span class="room-number">0${index + 1} / ${item.id}</span><span class="room-indicator ${occupied ? "room-indicator--busy" : ""}"><span></span>${occupied ? "Ocupada" : "Habilitada"}</span></div>
        <div class="room-graphic" aria-hidden="true"><span class="door-shape"><i></i></span><span class="room-lines"></span></div>
        <h2>Habitación ${item.id}</h2><p>${item.beds} cama${item.beds > 1 ? "s" : ""} · ${item.window ? "Vista exterior" : "Interior"}</p>
        <div class="room-meta"><span>${label}</span><strong>${next ? `${formatDate(next.date)} · ${next.time}` : "Disponible para reservar"}</strong></div>
        <button type="button" class="room-link" data-action="new-reservation" data-id="${item.id}">Crear reserva ${icon("arrow")}</button></article>`;
    }).join("")}</div>`;
}
function renderClients() {
  return `${pageHeader("RECEPCIÓN / CLIENTES", "Clientes", "Datos ficticios para recorrer el proceso de reserva.", button("Nuevo cliente", "new-client"))}
    <section class="panel list-panel"><div class="toolbar"><div class="toolbar-copy"><span class="eyebrow">DIRECTORIO</span><h2>Huéspedes registrados</h2></div><div class="filters"><label class="sr-only" for="clientSearch">Buscar cliente</label><input id="clientSearch" type="search" placeholder="Buscar nombre o documento" value="${escapeHtml(clientSearch)}" autocomplete="off"></div></div><div id="clientRows"></div></section>`;
}
function renderClientRows() {
  const target = document.getElementById("clientRows");
  if (!target) return;
  const query = clientSearch.trim().toLocaleLowerCase("es");
  const clients = state.clients.filter(item => `${item.name} ${item.document}`.toLocaleLowerCase("es").includes(query));
  target.innerHTML = clients.length ? `<div class="client-list">${clients.map(item => {
    const count = state.reservations.filter(reservation => reservation.clientId === item.id).length;
    return `<div class="client-row"><div class="avatar" aria-hidden="true">${escapeHtml(item.name.split(" ").map(part => part[0]).slice(0, 2).join("").toUpperCase())}</div><div><strong>${escapeHtml(item.name)}</strong><small>Documento de ejemplo · ${escapeHtml(item.document)}</small></div><span class="client-count">${count} reserva${count === 1 ? "" : "s"}</span><button type="button" class="row-button" data-action="new-reservation" data-id="" data-client="${item.id}">Reservar</button></div>`;
  }).join("")}</div>` : empty("Sin resultados", "Prueba otra búsqueda o registra un cliente.", "users");
}
function renderSales() {
  const total = Object.entries(cart).reduce((sum, [id, quantity]) => sum + (state.products.find(product => product.id === Number(id))?.price || 0) * quantity, 0);
  return `${pageHeader("COMERCIAL / VENTAS", "Ventas", "Registra una venta de ejemplo y observa cómo cambia el inventario.")}
    <div class="sales-layout"><section class="panel catalog-panel"><div class="panel-heading"><div><span class="eyebrow">CATÁLOGO</span><h2>Productos disponibles</h2></div><span class="panel-count">${state.products.length} productos</span></div>
      <div class="product-list">${state.products.map((product, index) => `<div class="product-row"><span class="product-index">0${index + 1}</span><div class="product-detail"><strong>${escapeHtml(product.name)}</strong><small>${product.stock} unidades en almacén</small></div><strong class="product-price">${currency(product.price)}</strong><button type="button" class="add-product" data-action="add-product" data-id="${product.id}" ${product.stock <= (cart[product.id] || 0) ? "disabled" : ""} aria-label="Agregar ${escapeHtml(product.name)} a la venta">${icon("plus")}</button></div>`).join("")}</div>
    </section>
    <section class="sale-panel"><div><span class="eyebrow eyebrow--light">VENTA EN CURSO</span><h2>Detalle de venta</h2><p>Agrega productos y confirma para actualizar el stock local.</p></div>
      <div class="cart-lines">${Object.entries(cart).length ? Object.entries(cart).map(([id, quantity]) => {
        const product = state.products.find(item => item.id === Number(id));
        if (!product) return "";
        return `<div class="cart-line"><div><strong>${escapeHtml(product.name)}</strong><small>${currency(product.price)} por unidad</small></div><div class="quantity"><button type="button" data-action="remove-product" data-id="${id}" aria-label="Quitar una unidad de ${escapeHtml(product.name)}">−</button><span>${quantity}</span><button type="button" data-action="add-product" data-id="${id}" ${product.stock <= quantity ? "disabled" : ""} aria-label="Agregar una unidad de ${escapeHtml(product.name)}">+</button></div></div>`;
      }).join("") : `<div class="cart-empty">Selecciona un producto para comenzar.</div>`}</div>
      <div class="sale-summary"><span>Total</span><strong>${currency(total)}</strong></div><button type="button" class="button button--light sale-submit" data-action="finish-sale" ${total <= 0 ? "disabled" : ""}>${icon("check")}<span>Registrar venta</span></button>
    </section></div>
    <section class="recent-sales"><div class="panel-heading"><div><span class="eyebrow">HISTORIAL LOCAL</span><h2>Ventas recientes</h2></div></div>
      ${state.sales.length ? `<div class="sales-history">${state.sales.slice().reverse().slice(0, 4).map(sale => `<div><span>#${sale.id} · ${formatDate(sale.date, {day:"numeric",month:"long",year:"numeric"})}</span><strong>${currency(sale.total)}</strong><small>${sale.items.map(item => `${item.quantity} × ${escapeHtml(item.name)}`).join(" · ")}</small></div>`).join("")}</div>` : empty("Sin ventas aún", "Registra la primera venta de esta demo.", "sale")}</section>`;
}
function openModal(html) {
  modal.innerHTML = html;
  modal.querySelector("form")?.addEventListener("submit", event => {
    event.preventDefault();
    if (event.target.id === "reservationForm") submitReservation(event.target);
    if (event.target.id === "clientForm") submitClient(event.target);
  });
  modal.showModal();
  document.body.classList.add("dialog-open");
  modal.querySelector("input, select, button")?.focus();
}
function closeModal() {
  if (modal.open) modal.close();
  document.body.classList.remove("dialog-open");
}
function field(form, name) { return form.elements.namedItem(name).value; }
function modalShell(kicker, title, body, submitText, formId) {
  const action = formId === "reservationForm" ? "submit-reservation" : "submit-client";
  return `<div class="modal-head"><div><span class="eyebrow">${kicker}</span><h2 id="modalTitle">${title}</h2></div><button class="icon-button" type="button" data-action="close-modal" aria-label="Cerrar">${icon("close")}</button></div><form id="${formId}" novalidate>${body}<div class="form-actions"><button class="button button--ghost" type="button" data-action="close-modal">Volver</button><button class="button button--primary" type="button" data-action="${action}">${icon("check")}<span>${submitText}</span></button></div></form>`;
}
function openReservation(id = 0, roomId = 0, clientId = 0) {
  const editing = state.reservations.find(item => item.id === Number(id));
  const suggestedRoom = state.rooms.find(item => available(item.id, addDays(1), "14:00", 3))?.id || state.rooms[0].id;
  const selectedRoom = roomId || editing?.roomId || suggestedRoom;
  const selectedClient = clientId || editing?.clientId || state.clients[0]?.id;
  if (!state.clients.length) {
    toast("Registra un cliente antes de crear una reserva.", "error");
    return openClient();
  }
  const body = `<input type="hidden" name="id" value="${editing?.id || ""}">
    <div class="form-grid">
      <label class="field field--wide">Cliente<select name="clientId" required>${state.clients.map(item => `<option value="${item.id}" ${item.id === Number(selectedClient) ? "selected" : ""}>${escapeHtml(item.name)}</option>`).join("")}</select></label>
      <label class="field">Habitación<select name="roomId" required>${state.rooms.filter(item => item.active).map(item => `<option value="${item.id}" ${item.id === Number(selectedRoom) ? "selected" : ""}>Habitación ${item.id} · ${item.beds} cama${item.beds > 1 ? "s" : ""}</option>`).join("")}</select></label>
      <label class="field">Estado<select name="status">${Object.entries(STATUS).map(([value, label]) => `<option value="${value}" ${(editing?.status || "pendiente") === value ? "selected" : ""}>${label}</option>`).join("")}</select></label>
      <label class="field">Fecha de ingreso<input name="date" type="date" min="${localDate()}" value="${editing?.date || addDays(1)}" required></label>
      <label class="field">Hora de ingreso<input name="time" type="time" value="${editing?.time || "14:00"}" required></label>
      <label class="field">Duración<select name="hours"><option value="3" ${editing?.hours === 3 ? "selected" : ""}>3 horas</option><option value="24" ${editing?.hours === 24 ? "selected" : ""}>24 horas</option></select></label>
      <label class="field">Huéspedes<input name="guests" type="number" min="1" max="8" value="${editing?.guests || 1}" required></label>
      <label class="field">Pago en efectivo (S/)<input name="cash" type="number" min="0" step="0.01" value="${editing?.cash ?? 0}" required></label>
      <label class="field">Pago por Yape (S/)<input name="yape" type="number" min="0" step="0.01" value="${editing?.yape ?? 0}" required></label>
    </div>
    <div class="estimate" id="estimate" aria-live="polite"></div>
    <p class="form-error" id="reservationError" role="alert" hidden></p>`;
  openModal(modalShell("RECEPCIÓN / RESERVAS", editing ? `Editar reserva #${editing.id}` : "Nueva reserva", body, editing ? "Guardar cambios" : "Registrar reserva", "reservationForm"));
  updateEstimate();
}
function updateEstimate() {
  const form = document.getElementById("reservationForm");
  if (!form) return;
  const cost = priceFor(room(field(form, "roomId")), field(form, "hours"));
  const paid = (Number(field(form, "cash")) || 0) + (Number(field(form, "yape")) || 0);
  document.getElementById("estimate").innerHTML = `<span>Tarifa de estancia <strong>${currency(cost)}</strong></span><span>Saldo pendiente <strong>${currency(Math.max(0, cost - paid))}</strong></span>`;
}
function openClient() {
  const body = `<div class="form-grid"><label class="field field--wide">Nombre completo<input name="name" type="text" minlength="3" maxlength="80" placeholder="Ej. Elena Díaz" required></label><label class="field field--wide">Documento ficticio<input name="document" type="text" inputmode="numeric" pattern="[0-9]{8}" maxlength="8" placeholder="00000005" required></label></div><p class="form-note">Usa solo datos ficticios. Esta demo guarda la información en tu navegador.</p><p class="form-error" id="clientError" role="alert" hidden></p>`;
  openModal(modalShell("RECEPCIÓN / CLIENTES", "Nuevo cliente", body, "Guardar cliente", "clientForm"));
}
function showFormError(id, message) {
  const target = document.getElementById(id);
  target.textContent = message;
  target.hidden = false;
}
function submitReservation(form) {
  const item = {
    id: Number(field(form, "id")) || Math.max(1000, ...state.reservations.map(value => value.id)) + 1,
    clientId: Number(field(form, "clientId")),
    roomId: Number(field(form, "roomId")),
    date: field(form, "date"),
    time: field(form, "time"),
    hours: Number(field(form, "hours")),
    guests: Number(field(form, "guests")),
    cash: Number(field(form, "cash")),
    yape: Number(field(form, "yape")),
    status: field(form, "status")
  };
  const existing = state.reservations.some(value => value.id === item.id);
  const cost = priceFor(room(item.roomId), item.hours);
  if (!client(item.clientId) || !room(item.roomId) || !item.date || !item.time || ![3, 24].includes(item.hours)
      || !Number.isInteger(item.guests) || item.guests < 1 || item.guests > 8 || !STATUS[item.status]) {
    return showFormError("reservationError", "Revisa los datos de la reserva.");
  }
  if (!existing && dateTime(item.date, item.time).getTime() < Date.now()) {
    return showFormError("reservationError", "El ingreso debe ser en una fecha y hora futuras.");
  }
  if (![item.cash, item.yape].every(value => Number.isFinite(value) && value >= 0) || item.cash + item.yape > cost + 0.001) {
    return showFormError("reservationError", "Los pagos deben ser válidos y no superar la tarifa.");
  }
  if (isActive(item) && !available(item.roomId, item.date, item.time, item.hours, item.id)) {
    return showFormError("reservationError", "La habitación ya está reservada en ese horario. Prueba otra fecha u otra habitación.");
  }
  if (existing) state.reservations = state.reservations.map(value => value.id === item.id ? item : value);
  else state.reservations.push(item);
  saveState();
  closeModal();
  go("reservas");
  toast(existing ? "Reserva actualizada." : "Reserva registrada.");
}
function submitClient(form) {
  const name = field(form, "name").trim().replace(/\s+/g, " ");
  const document = field(form, "document").trim();
  if (name.length < 3 || !/^[0-9]{8}$/.test(document)) {
    return showFormError("clientError", "Indica un nombre y un documento ficticio de 8 dígitos.");
  }
  if (state.clients.some(item => item.document === document)) {
    return showFormError("clientError", "Ese documento ya está registrado.");
  }
  state.clients.push({ id: Math.max(0, ...state.clients.map(item => item.id)) + 1, name, document });
  saveState();
  closeModal();
  go("clientes");
  toast("Cliente registrado.");
}
function confirmCancel(id) {
  const item = state.reservations.find(value => value.id === Number(id));
  if (!item || !isActive(item)) return;
  openModal(`<div class="modal-head"><div><span class="eyebrow">CONFIRMAR ACCIÓN</span><h2 id="modalTitle">Cancelar reserva #${item.id}</h2></div><button class="icon-button" type="button" data-action="close-modal" aria-label="Cerrar">${icon("close")}</button></div><p class="confirm-copy">La estancia de <strong>${escapeHtml(client(item.clientId)?.name || "este cliente")}</strong> dejará de bloquear la habitación ${item.roomId}. Puedes reiniciar toda la demo desde la barra superior.</p><div class="form-actions"><button class="button button--ghost" type="button" data-action="close-modal">Volver</button><button class="button button--danger" type="button" data-action="confirm-cancel" data-id="${item.id}">Cancelar reserva</button></div>`);
}
function confirmReset() {
  openModal(`<div class="modal-head"><div><span class="eyebrow">DATOS DE EJEMPLO</span><h2 id="modalTitle">Reiniciar demo</h2></div><button class="icon-button" type="button" data-action="close-modal" aria-label="Cerrar">${icon("close")}</button></div><p class="confirm-copy">Se restaurarán las habitaciones, clientes, reservas y ventas de ejemplo de este navegador.</p><div class="form-actions"><button class="button button--ghost" type="button" data-action="close-modal">Volver</button><button class="button button--danger" type="button" data-action="confirm-reset">Reiniciar datos</button></div>`);
}
function closeMenu() {
  sidebar.classList.remove("is-open");
  document.body.classList.remove("menu-open");
  menuButton.setAttribute("aria-expanded", "false");
}
function onAction(action, element) {
  const id = Number(element.dataset.id);
  switch (action) {
    case "navigate": go(element.dataset.id); break;
    case "new-reservation": openReservation(0, id || 0, Number(element.dataset.client) || 0); break;
    case "edit-reservation": openReservation(id); break;
    case "new-client": openClient(); break;
    case "submit-reservation": submitReservation(document.getElementById("reservationForm")); break;
    case "submit-client": submitClient(document.getElementById("clientForm")); break;
    case "cancel-reservation": confirmCancel(id); break;
    case "confirm-cancel": {
      const item = state.reservations.find(value => value.id === id);
      if (!item) break;
      item.status = "cancelada";
      saveState(); closeModal(); render(); toast("Reserva cancelada.");
      break;
    }
    case "add-product": {
      const product = state.products.find(value => value.id === id);
      if (!product || product.stock <= (cart[id] || 0)) break;
      cart[id] = (cart[id] || 0) + 1;
      main.innerHTML = renderSales();
      break;
    }
    case "remove-product":
      if (cart[id]) {
        cart[id]--;
        if (cart[id] <= 0) delete cart[id];
        main.innerHTML = renderSales();
      }
      break;
    case "finish-sale": {
      const items = Object.entries(cart).map(([productId, quantity]) => {
        const product = state.products.find(value => value.id === Number(productId));
        return product && quantity > 0 && product.stock >= quantity
          ? { productId: product.id, name: product.name, quantity, price: product.price } : null;
      });
      if (!items.length || items.some(item => !item)) return toast("Revisa el stock antes de registrar la venta.", "error");
      items.forEach(item => { state.products.find(product => product.id === item.productId).stock -= item.quantity; });
      state.sales.push({ id: Math.max(500, ...state.sales.map(sale => sale.id)) + 1, date: localDate(), items,
        total: items.reduce((sum, item) => sum + item.price * item.quantity, 0) });
      cart = {};
      saveState(); render(); toast("Venta registrada. El stock se actualizó.");
      break;
    }
    case "confirm-reset":
      state = seedState(); cart = {}; reservationFilter = { search: "", status: "todas" }; clientSearch = "";
      saveState(); closeModal(); render(); toast("Datos de ejemplo restaurados.");
      break;
    case "close-modal": closeModal(); break;
  }
}
document.addEventListener("click", event => {
  const actionElement = event.target.closest("[data-action]");
  if (actionElement) onAction(actionElement.dataset.action, actionElement);
});
document.addEventListener("input", event => {
  if (event.target.id === "reservationSearch") { reservationFilter.search = event.target.value; renderReservationRows(); }
  if (event.target.id === "clientSearch") { clientSearch = event.target.value; renderClientRows(); }
  if (event.target.closest("#reservationForm")) {
    const error = document.getElementById("reservationError");
    if (error) error.hidden = true;
    updateEstimate();
  }
  if (event.target.closest("#clientForm")) {
    const error = document.getElementById("clientError");
    if (error) error.hidden = true;
  }
});
document.addEventListener("change", event => {
  if (event.target.id === "reservationStatus") { reservationFilter.status = event.target.value; renderReservationRows(); }
});
modal.addEventListener("close", () => document.body.classList.remove("dialog-open"));
modal.addEventListener("click", event => { if (event.target === modal) closeModal(); });
menuButton.addEventListener("click", () => {
  const open = sidebar.classList.toggle("is-open");
  document.body.classList.toggle("menu-open", open);
  menuButton.setAttribute("aria-expanded", String(open));
});
document.getElementById("sidebarBackdrop").addEventListener("click", closeMenu);
document.getElementById("resetButton").addEventListener("click", confirmReset);
window.addEventListener("hashchange", () => { render(); window.scrollTo(0, 0); });
render();

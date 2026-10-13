(() => {
  const body = document.body;
  const menuButton = document.querySelector('[data-toggle-sidebar]');
  const closeButton = document.querySelector('[data-close-sidebar]');
  const sidebar = document.getElementById('appSidebar');

  function closeMenu() {
    body.classList.remove('sidebar-open');
    menuButton?.setAttribute('aria-expanded', 'false');
  }
  menuButton?.addEventListener('click', () => {
    const open = !body.classList.contains('sidebar-open');
    body.classList.toggle('sidebar-open', open);
    menuButton.setAttribute('aria-expanded', String(open));
    if (open) sidebar?.querySelector('a')?.focus();
  });
  closeButton?.addEventListener('click', closeMenu);
  document.addEventListener('keydown', event => {
    if (event.key === 'Escape') closeMenu();
  });
  sidebar?.addEventListener('click', event => {
    if (event.target.closest('a')) closeMenu();
  });

  const path = decodeURIComponent(location.pathname.split('/').pop() || '').toLowerCase();
  let section = 'inicio';
  if (path.includes('movimiento')) section = 'movimientos';
  else if (path.includes('proveedor')) section = 'proveedores';
  else if (path.includes('lineaventa') || path.includes('venta')) section = 'ventas';
  else if (path.includes('lineacompra') || path.includes('compra')) section = 'compras';
  else if (path.includes('producto')) section = 'productos';
  else if (path.includes('reserva')) section = 'reservas';
  else if (path.includes('habitacion')) section = 'habitaciones';
  else if (path.includes('cliente')) section = 'clientes';
  else if (path.includes('empleado')) section = 'empleados';
  const activeLink = document.querySelector('[data-nav="' + section + '"]');
  activeLink?.classList.add('is-active');
  activeLink?.setAttribute('aria-current', 'page');

  const scrollButton = document.querySelector('[data-scroll-top]');
  function updateScrollButton() {
    scrollButton?.classList.toggle('is-visible', window.scrollY > 460);
  }
  window.addEventListener('scroll', updateScrollButton, { passive: true });
  updateScrollButton();
  scrollButton?.addEventListener('click', () => {
    window.scrollTo({ top: 0, behavior: window.matchMedia('(prefers-reduced-motion: reduce)').matches ? 'auto' : 'smooth' });
  });

  const dialog = document.getElementById('actionConfirm');
  let pendingForm = null;
  dialog?.querySelector('[data-cancel-confirm]')?.addEventListener('click', () => {
    pendingForm = null;
    dialog.close();
  });
  dialog?.querySelector('[data-accept-confirm]')?.addEventListener('click', () => {
    const form = pendingForm;
    pendingForm = null;
    dialog.close();
    if (form) {
      form.dataset.confirmed = 'true';
      form.requestSubmit();
    }
  });
  dialog?.addEventListener('close', () => { pendingForm = null; });

  document.addEventListener('submit', event => {
    const form = event.target;
    if (!(form instanceof HTMLFormElement)) return;
    if (form.dataset.confirm && form.dataset.confirmed !== 'true') {
      event.preventDefault();
      if (!dialog?.showModal) {
        if (window.confirm(form.dataset.confirm)) {
          form.dataset.confirmed = 'true';
          form.requestSubmit();
        }
        return;
      }
      pendingForm = form;
      dialog.querySelector('[data-confirm-message]').textContent = form.dataset.confirm;
      dialog.showModal();
      return;
    }
    if (form.method.toLowerCase() === 'post') {
      const submitter = event.submitter || form.querySelector('[type=submit]');
      if (submitter) {
        window.setTimeout(() => {
          submitter.classList.add('is-busy');
          submitter.setAttribute('aria-busy', 'true');
          submitter.disabled = true;
        }, 0);
      }
    }
  }, true);

  document.querySelectorAll('[data-password-toggle]').forEach(button => {
    button.addEventListener('click', () => {
      const input = document.getElementById(button.dataset.passwordToggle);
      if (!input) return;
      const visible = input.type === 'password';
      input.type = visible ? 'text' : 'password';
      button.setAttribute('aria-pressed', String(visible));
      button.setAttribute('aria-label', visible ? 'Ocultar clave' : 'Mostrar clave');
      button.querySelector('i')?.classList.toggle('fa-eye-slash', visible);
      button.querySelector('i')?.classList.toggle('fa-eye', !visible);
    });
  });

  function baseCost() {
    const roomSelect = document.getElementById('roomNumber');
    const hoursSelect = document.getElementById('reservedHours');
    if (!roomSelect || !roomSelect.value || !hoursSelect || !roomSelect.selectedOptions.length) return null;
    const option = roomSelect.selectedOptions[0];
    const beds = Number(option.dataset.camas);
    const windowOutside = option.dataset.ventana === 'true';
    if (hoursSelect.value === '3') return windowOutside ? 25 : 20;
    if (hoursSelect.value === '24') return beds === 2 ? 70 : windowOutside ? 50 : 40;
    return null;
  }
  window.calcularCosto = () => {
    const target = document.getElementById('costResult');
    const cost = baseCost();
    if (target) {
      target.value = cost === null ? '' : cost.toFixed(2);
      target.placeholder = cost === null ? 'Selecciona habitación' : '';
      target.dispatchEvent(new Event('change', { bubbles: true }));
    }
  };
  window.calcularNuevoCosto = () => {
    const target = document.getElementById('newCostResult');
    const extra = Number(document.getElementById('additionalHours')?.value || 0);
    const hours = Number(document.getElementById('reservedHours')?.value || 0);
    if (!target || !Number.isFinite(extra) || extra < 0 || !hours) return;
    const cost = extra > 2 * hours ? baseCost() : extra * 10;
    if (cost !== null) target.value = cost.toFixed(2);
  };
  document.querySelectorAll('#roomNumber, #reservedHours').forEach(input => {
    input.addEventListener('change', () => {
      if (document.getElementById('costResult')) window.calcularCosto();
    });
  });
  if (document.getElementById('costResult')) window.calcularCosto();
})();
